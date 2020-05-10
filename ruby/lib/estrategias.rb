require_relative 'metodos'
class Estrategia
  attr_accessor :nombre, :bloque
  def initialize(&bloque)
    self.instance_eval(&bloque)
  end

  def self.define(&bloque)
    nueva_estrategia = Estrategia.new(&bloque)
    Object.const_set(nueva_estrategia.nombre, nueva_estrategia)
  end

  def name(simbolo)
    @nombre = simbolo
  end

  def bloque (&bloque)
    @bloque = bloque
  end

  def aplicar (metodo_arreglar)
    metodo_conflictivo = metodo_arreglar
    bloque_clase = @bloque
    nuevo_bloque = proc do
      bloque_clase.call
    end
    MetodoTrait.new(metodo_conflictivo.nombre,&nuevo_bloque)
  end
end
=begin
class OrdenDeAparicion
  def aplicar(metodo_conflictivo)
    nuevo_bloque = proc do |*params,&bloque|
      metodo_conflictivo.codigo1.call(*params,&bloque)
      metodo_conflictivo.codigo2.call(*params,&bloque)
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end
=end
=begin
orden_de_aparicion = proc do |*params,&bloque|
  metodo_conflictivo = params[0]
  parametros = params[1..-1]
  metodo_conflictivo.codigo1.call(*parametros,&bloque)
  metodo_conflictivo.codigo2.call(*parametros,&bloque)
  return MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
end

aplicar_funcion = proc do |metodo_conflictivo,funcion,*params,&bloque|
  res1 = metodo_conflictivo.codigo1.call(*params,&bloque)
  res2 = metodo_conflictivo.codigo2.call(*params,&bloque)
  [res2].reduce(res1, funcion)
  return MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
end

aplicar_por_condicion = proc do |*params,&bloque|
  [params[0].codigo1,params[0].codigo2].each do |m|
    res = m.call(params[2..-1],&bloque)
    if params[1].call(res)
      return res
    end
    return MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end
=end

=begin
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
=end
=begin
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
=end