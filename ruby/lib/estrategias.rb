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

  def att(*atributos)
    atributos.each { |atributo| self.singleton_class.send(:attr_accessor, atributo)}
  end

  def bloque(&bloque)
    @bloque = bloque
  end

  def aplicar(metodo_conflictivo)
    bloque_clase = @bloque
    nuevo_bloque = proc do |*params|
      bloque_clase.call(metodo_conflictivo, *params)
    end
    MetodoTrait.new(metodo_conflictivo.nombre, &nuevo_bloque)
  end
end

=begin
Estrategia.define do
  name :OrdenDeAparicion
  bloque do |metodo_conflictivo,*params|
    metodo_conflictivo.codigo1.call(*params)
    metodo_conflictivo.codigo2.call(*params)
  end
end
=end

=begin
Estrategia.define do
  name :AplicarFuncion
  att :funcion
  bloque do |metodo_conflictivo,*params|
    res1 = metodo_conflictivo.codigo1.call(*params)
    res2 = metodo_conflictivo.codigo2.call(*params)
    [res2].reduce(res1, :funcion)
  end
end
=end

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

OrdenDeAparicion = lambda do |metodo_conflictivo, *params|
  metodo_conflictivo.codigo1.call(*params)
  metodo_conflictivo.codigo2.call(*params)
end

AplicarFuncion = lambda do |funcion, metodo_conflictivo, *params|
  res1 = metodo_conflictivo.codigo1.call(*params)
  res2 = metodo_conflictivo.codigo2.call(*params)
  [res2].reduce(res1, funcion)
end

AplicarFunciony = AplicarFuncion.curry

AplicarPorCondicion = lambda do |condicion, metodo_conflictivo, *params|
  [metodo_conflictivo.codigo1, metodo_conflictivo.codigo2].each do |m|
    res = m.call(*params)
    if condicion.call(res)
      return res
    end
  end
end

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