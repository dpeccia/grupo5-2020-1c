require_relative 'metodos'

OrdenDeAparicion = lambda do |metodo_conflictivo, *params|
  metodo_conflictivo.codigo1.call(*params)
  metodo_conflictivo.codigo2.call(*params)
end

AplicarFuncion = lambda do |funcion, metodo_conflictivo, *params|
  res1 = metodo_conflictivo.codigo1.call(*params)
  res2 = metodo_conflictivo.codigo2.call(*params)
  [res2].reduce(res1, funcion)
end.curry

AplicarPorCondicion = lambda do |condicion, metodo_conflictivo, *params|
  [metodo_conflictivo.codigo1, metodo_conflictivo.codigo2].each do |m|
    res = m.call(*params)
    if condicion.call(res)
      return res
    end
  end
end.curry

EstrategiaNuevaDefinidaPorElUsuario = lambda do |numero, otro_numero, metodo_conflictivo, *params|
  res1 = metodo_conflictivo.codigo1.call(*params)
  res2 = metodo_conflictivo.codigo2.call(*params)
  res1 + res2 + numero + otro_numero
end.curry