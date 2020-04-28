class Trait
  attr_reader :nombre
  attr_accessor :bloque
  @@id = 1
  def initialize(&bloque)
    self.instance_eval(&bloque)
  end

  def self.define(&bloque)
    nuevoTrait = Trait.new(&bloque)
    nuevoTrait.bloque = bloque
    Object.const_set(nuevoTrait.nombre,nuevoTrait)
  end
  
  def - simbolo 

    nuevo_trait = Trait.new(&self.bloque)

    nuevo_trait.singleton_class.send :remove_method,simbolo

    return nuevo_trait

  end


  def name(simbolo)
    @nombre = simbolo
  end

  def method(simbolo, &bloque)
    define_singleton_method(simbolo, &bloque)
  end
  def + otro_trait
    nuevo_trait = Trait.new(&self.bloque)
    Object.const_set("NuevoTrait#{@@id}".to_sym,nuevo_trait)
    @@id += 1
    error = proc {raise 'Metodo Repetido'}
    otro_trait.singleton_methods(false).each do |m|
      if nuevo_trait.methods(false).include? m
        nuevo_trait.define_singleton_method(m, &error) 
        next
      end
      nuevo_trait.define_singleton_method(m, &otro_trait.singleton_method(m))
    end
    return nuevo_trait
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
<<<<<<< HEAD
A.new.metodo2 2
=======
A.new.metodo2 2
>>>>>>> d27962834ac11edceb80a1726d0f38a8476856b9
