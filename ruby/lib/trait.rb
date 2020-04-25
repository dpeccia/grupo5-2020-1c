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
    Trait.new()
    @error = proc {raise 'Metodos duplicados'}
    @otro_trait = otro_trait
  end
  def method_missing(m, *args, &block)
    @otro_trait.send(m,*args,&block)
  end
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

