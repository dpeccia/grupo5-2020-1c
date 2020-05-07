require_relative 'metodos'

class OrdenDeAparicion
  def aplicar(metodo_conflictivo)
    nuevo_bloque = proc do |*params,&bloque|
      metodo_conflictivo.codigo1.call(*params,&bloque)
      metodo_conflictivo.codigo2.call(*params,&bloque)
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end

class AplicarFuncion
  attr_reader :funcion
  def initialize(simbolo)
    @funcion = simbolo
  end

  def aplicar(metodo_conflictivo)
    metodo = @funcion
    nuevo_bloque = proc do |*params,&bloque|
      res1 = metodo_conflictivo.codigo1.call(*params,&bloque)
      res2 = metodo_conflictivo.codigo2.call(*params,&bloque)
      [res2].reduce(res1, metodo)
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end

class AplicarPorCondicion
  attr_accessor :condicion
  def initialize(condicion_)
    @condicion = condicion_
  end

  def aplicar(metodo_conflictivo)
    metodo = @condicion
    nuevo_bloque = proc do |*params,&bloque|
      [metodo_conflictivo.codigo1,metodo_conflictivo.codigo2].each do |m|
        res = m.call(*params,&bloque)
        if metodo.call(res)
          return res
        end
      end
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end
