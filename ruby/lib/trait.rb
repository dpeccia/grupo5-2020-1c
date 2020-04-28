class Trait
  attr_reader :nombre

  def initialize(&bloque)
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
    define_singleton_method(simbolo, &bloque)
  end

  def +(otro_trait)
    nuevo_trait = self.clone
    error = proc {raise 'Metodo Repetido'}
    otro_trait.singleton_methods(false).each do |m|
      if nuevo_trait.methods(false).include? m
        nuevo_trait.singleton_class.remove_method(m)
        nuevo_trait.define_singleton_method(m, &error)
      else
        nuevo_trait.define_singleton_method(m, &otro_trait.singleton_method(m))
      end
    end
    nuevo_trait
  end

  def -(simbolo)
    nuevo_trait = self.clone
    errorDeMetodo = proc {raise 'Solo remueve metodos incluidos en su trait'}
    if not nuevo_trait.methods(false).include? simbolo
      errorDeMetodo.call
    else
      nuevo_trait.singleton_class.send :remove_method,simbolo
    end
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