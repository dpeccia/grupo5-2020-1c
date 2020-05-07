require_relative 'metodos'

class OrdenDeAparicion
  def self.aplicar(metodo_conflictivo)
    nuevo_bloque = proc do |*params,&bloque|
      metodo_conflictivo.codigo1.call(*params,&bloque)
      metodo_conflictivo.codigo2.call(*params,&bloque)
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end
