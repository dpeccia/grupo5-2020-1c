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
    nuevo_trait.singleton_class.send :remove_method,simbolo
    nuevo_trait
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


Trait.define do
  name :MiTrait
  method :metodo1 do
    "Hola"
  end
  method :metodo2 do |un_numero|
    un_numero * 0 + 42
  end
end

Trait.define do
  name :MiOtroTrait
  method :metodo1 do
  "kawuabonga" 
  end
  method :metodo3 do
  "zaraza" 
  end 
end

class A
  uses MiTrait + (MiOtroTrait - :metodo1)
end

puts A.new.metodo3
puts A.new.metodo2 2
puts A.new.metodo1
puts MiTrait.methods false
