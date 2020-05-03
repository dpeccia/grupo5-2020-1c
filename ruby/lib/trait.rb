class Trait
  attr_reader :nombre
  attr_accessor :metodos
  @@metodos_duplicados = proc {raise 'Metodo Repetido'}
  @@error_metodo_no_incluido = proc {raise 'Solo remueve metodos incluidos en su trait'}

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

  def self.sumar_traits(un_trait,otro_trait)
    nuevo_trait = Trait.new
    nuevo_trait.metodos = un_trait.metodos.merge(otro_trait.metodos){|key, oldval, newval| @@metodos_duplicados}
    return nuevo_trait
  end

  def +(otro_trait)
    self.class.sumar_traits(self,otro_trait)
  end

  def -(simbolo)
    nuevo_trait = self.clone
    if nuevo_trait.methods(false).include? simbolo
      nuevo_trait.singleton_class.send :remove_method,simbolo
    else
      @@error_metodo_no_incluido.call
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
    trait.metodos.each do |key,value|
      unless self.instance_methods(false).include? key
        self.define_method(key, &value)
      end
    end
  end
end