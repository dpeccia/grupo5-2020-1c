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
  def uses un_trait
    @@un_trait = un_trait
  end
  def method_missing(m, *args, &block)
    @@un_trait.send(m,*args,&block) unless @@un_trait.nil?
    super()
  end
end

