class Trait
  attr_accessor :nombre,:metodos,:metodos_repetidos
  def self.define(iniciadores)
    @nombre = iniciadores.match(/(?<=name :).+/)
    nombre_metodos = iniciadores.scan(/(?<=method :).+(?=.do)/)
    create_method(hash[:method],block)
  end
  def create_method(name, &block)
    self.class.send(:define_method, name, &block)
  end

  def +(otro_trait)
    #metodos_repetidos = otro_trait.metodos.detect{ |m| self.metodos.count(m) > 1 }
    otro_trait.methods.each do |m|
      if self.methods.include? m

      end
        self.class.send(:define_method, name, &block) do
          block = {do |*arg| raise "Metodos Con nombres iguales, fijarse de arreglarlo" end}
        next
      end
      define_method (m) do |*arg|
        otro_trait.send(m,*arg)
      end
    end
      #return self
  end


end
class Class
  def uses un_trait
    include un_trait
  end
end

