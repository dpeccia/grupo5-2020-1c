class Trait
  attr_reader :nombre

  def initialize(&bloque)
    self.instance_eval(&bloque)
  end

  def self.define(&bloque)
    nuevoTrait = Trait.new(&bloque)
    Object.const_set(nuevoTrait.nombre,nuevoTrait)
  end


  def name(simbolo)
    @nombre = simbolo
  end

  def method(simbolo, &bloque)
    define_singleton_method(simbolo, &bloque)
  end
  def + otro_trait
    error = proc {raise 'Metodo Repetido'}
    otro_trait.singleton_methods(false).each do |m|
      if self.methods(false).include? m
        self.define_singleton_method(m, &error) 
        next
      end
      self.define_singleton_method(m, &otro_trait.singleton_method(m))
    end
    return self
  end
  #def method_missing(m, *args, &block)
   # @otro_trait.send(m,*args,&block)
  #end
end

class Class
  def uses(trait)
    trait.singleton_methods(false).each { |met|
      unless(self.instance_methods(false).include? met)
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

class A uses MiTrait + MiOtroTrait 
end

A.new.metodo1
A.new.metodo3
A.new.metodo2 2
