require_relative 'excepciones'
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
    nuevos_metodos = @metodos + otro_trait.metodos
    self.class.crear_trait(nuevos_metodos)
  end

  def -(simbolo)
    nuevos_metodos = @metodos.select{|m| m.nombre != simbolo }
    if nuevos_metodos.length == self.metodos.length
      raise MetodoRemovidoNoExiste
    end
    self.class.crear_trait(nuevos_metodos)
  end

  def <<(hash)
    nuevos_metodos = self.metodos
    unless nuevos_metodos.map { |m| m.nombre }.include? hash[:metodo_copiado]
      raise MetodoRenombradoNoExiste
    end
    nuevos_metodos = self.metodos + [MetodoTrait.new(hash[:nuevo_nombre], &nuevos_metodos.detect{|m| m.es_mi_nombre? hash[:metodo_copiado]}.codigo)]
    self.class.crear_trait(nuevos_metodos)
  end

  def >>(estrategia)
    metodos_no_conflictivos = obtener_metodos_no_conflictivos

    obtener_metodos_conflictivos.each do |metodo_conflictivo|
      nuevo_bloque = lambda do |*params|
        estrategia.call(metodo_conflictivo, *params)
      end
      metodos_no_conflictivos << MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
    end

    nuevos_metodos = metodos_no_conflictivos
    self.class.crear_trait(nuevos_metodos)
  end

  private

  def self.crear_trait(metodos)
    nuevo_trait = Trait.new
    nuevo_trait.metodos = metodos
    nuevo_trait
  end

  def obtener_metodos_conflictivos
    self.metodos.select{|metodo| esta_duplicado(metodo)}
        .group_by{|m| m.nombre}.map{|m| MetodoConflictivo.new(m[0], [m[1][0], m[1][1]])}
  end

  def obtener_metodos_no_conflictivos
    self.metodos.select{|metodo| !esta_duplicado(metodo)}
  end

  def esta_duplicado(metodo)
    self.metodos.map{|m| m.nombre}.count(metodo.nombre) > 1
  end
end

class Class
  def uses(trait)
    nombres_metodos = trait.metodos.map {|m| m.nombre}
    if(nombres_metodos.any?{ |m| nombres_metodos.count(m) > 1 })
      raise ConflictosSinResolver
    else
      trait.metodos.each do |m|
        unless self.instance_methods(false).include? m.nombre
          self.define_method(m.nombre, &m.codigo)
        end
      end
    end
  end
end

