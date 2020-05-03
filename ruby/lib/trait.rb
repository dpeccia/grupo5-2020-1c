require_relative 'trait_symbol'
require_relative 'estrategias'
class Trait
  attr_reader :nombre
  attr_accessor :metodos,:estrategia
  @@metodos_duplicados = proc {raise 'Metodo Repetido'}
  @@error_metodo_no_incluido = proc {raise 'Solo remueve metodos incluidos en su trait'}
  @@no_existe_metodo =  proc {raise 'Solo puede renombrar metodos incluidos en el trait'}
  def initialize(&bloque)
    @metodos = {}
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
    @metodos[simbolo] = bloque
  end

  def self.crear_trait(metodos)
    nuevo_trait = Trait.new
    nuevo_trait.metodos = metodos
    nuevo_trait
  end

  def +(otro_trait)
    metodos = self.metodos.merge(otro_trait.metodos) do |key, oldval, newval|
      bloque = @@metodos_duplicados
      unless @estrategia.nil?
        bloque = @estrategia.aplicar(oldval,newval)
      end
      bloque
    end
    self.class.crear_trait(metodos)
  end

  def -(simbolo)
    metodos = self.metodos.select{|m| m!= simbolo }
    if metodos.length == self.metodos.length
      @@error_metodo_no_incluido.call
    end
    self.class.crear_trait(metodos)
  end
  #using TraitSymbol
  def << un_hash
    metodos_trait = self.metodos
    if !metodos_trait.keys.include? un_hash[:metodo_copiado]
      @@no_existe_metodo.call
    end
    metodos_trait[un_hash[:nuevo_nombre]] = metodos_trait[un_hash[:metodo_copiado]]
    self.class.crear_trait(metodos_trait)
  end

end

class Class
  def uses(trait)
    trait.metodos.each do |key,value|
      unless self.instance_methods(false).include? key
        self.define_method(key, &value)
      end
    end
  end
end