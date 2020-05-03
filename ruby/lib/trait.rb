class Trait
  attr_reader :nombre
  attr_accessor :metodos

  def initialize(&bloque)
    @metodos = {}
    self.instance_eval(&bloque)
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

  def self.sumar_traits(un_trait,otro_trait)
    return un_trait
  end

  def +(otro_trait)
    self.class.sumar_traits(self,otro_trait)
  end

  def -(simbolo)
    nuevo_trait = self.clone
    error_de_metodo = proc {raise 'Solo remueve metodos incluidos en su trait'}
    if nuevo_trait.methods(false).include? simbolo
      nuevo_trait.singleton_class.send :remove_method,simbolo
    else
      error_de_metodo.call
    end
    nuevo_trait
  end

  def << un_hash
    nuevo_trait = self.clone
    nuevo_trait.method(un_hash[:nuevo_nombre],&nuevo_trait.singleton_method(un_hash[:metodo_copiado]))
    nuevo_trait
  end

end

class Symbol
  def >> un_simbolo
    {:nuevo_nombre => un_simbolo, :metodo_copiado => self}
  end
end

class Class
  def uses(trait)
    trait.singleton_methods(false).each { |met|
      unless self.instance_methods(false).include? met
        self.define_method(met, &trait.singleton_method(met))
      end
    }
  end
end