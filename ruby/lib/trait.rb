require_relative 'estrategias'
require_relative 'metodos'
require_relative 'symbol_trait'

class Trait
  attr_reader :nombre
  attr_accessor :metodos

  def initialize(&bloque)
    @metodos = []
    self.instance_eval(&bloque) unless bloque.nil?
  end

  def self.define(&bloque)
    nuevo_trait = Trait.new(&bloque)
    Object.const_set(nuevo_trait.nombre, nuevo_trait)
  end

  def name(simbolo)
    @nombre = simbolo
  end

  def method(simbolo, &bloque)
    @metodos << MetodoTrait.new(simbolo, &bloque)
  end

  def +(otro_trait)
    TraitBuilder.crear_trait(self.metodos) { |builder| builder.sumar(otro_trait) }
  end

  def -(simbolo)
    TraitBuilder.crear_trait(self.metodos) { |builder| builder.restar(simbolo) }
  end

  def <<(hash)
    TraitBuilder.crear_trait(self.metodos) do |builder|
      builder.renombrar(hash)
    end
  end

  def &(estrategia)
    TraitBuilder.crear_trait(self.metodos) { |builder| builder.resolver_conflictos(estrategia) }
  end

end

class TraitBuilder
  @@metodos_duplicados = proc {raise 'Metodo Repetido'}
  @@error_metodo_no_incluido = proc {raise 'Solo remueve metodos incluidos en su trait'}
  @@no_existe_metodo =  proc {raise 'Solo puede renombrar metodos incluidos en el trait'}

  def self.crear_trait(metodos)
    builder = new(metodos)
    yield(builder)
    builder.trait
  end

  def initialize(metodos)
    @trait = Trait.new
    @trait.metodos = metodos
  end

  def metodos
    @trait.metodos
  end

  def trait
    @trait
  end

  def sumar(otro_trait)
    @trait.metodos = @trait.metodos + otro_trait.metodos
  end

  def restar(metodo)
    metodos = @trait.metodos.select{|m| m.nombre != metodo }
    if metodos.length == trait.metodos.length
      @@error_metodo_no_incluido.call
    end
    @trait.metodos = metodos
  end

  def renombrar(hash)
    metodos_trait = @trait.metodos
    if !metodos_trait.map{|m| m.nombre}.include? hash[:metodo_copiado]
      @@no_existe_metodo.call
    end
    @trait.metodos << MetodoTrait.new(hash[:nuevo_nombre], &metodos_trait.detect{|m| m.es_mi_nombre?}.codigo)
  end

  def resolver_conflictos(estrategia)
    metodos_no_conflictivos = self.obtener_metodos_no_conflictivos
    self.obtener_metodos_conflictivos.each do |metodo_conflictivo|
      metodos_no_conflictivos << estrategia.aplicar(metodo_conflictivo)
    end
    @trait.metodos = metodos_no_conflictivos
  end

  def obtener_metodos_conflictivos
    @trait.metodos.select{|metodo| esta_duplicado(metodo)}
        .group_by{|m| m.nombre}.map{|m| MetodoConflictivo.new(m[0], [m[1][0], m[1][1]])}
  end

  def obtener_metodos_no_conflictivos
    @trait.metodos.select{|metodo| !esta_duplicado(metodo)}
  end

  def esta_duplicado(metodo)
    @trait.metodos.map{|m| m.nombre}.count(metodo.nombre) > 1
  end

end

class Class
  def uses(trait)
    nombres_metodos = trait.metodos.map {|m| m.nombre}
    if(nombres_metodos.any?{ |m| nombres_metodos.count(m) > 1 })
      raise "Tiene conflictos sin resolver"
    else
      trait.metodos.each do |m|
        unless self.instance_methods(false).include? m.nombre
          self.define_method(m.nombre, &m.codigo)
        end
      end
    end
  end
end

