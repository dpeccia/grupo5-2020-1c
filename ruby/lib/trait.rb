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
