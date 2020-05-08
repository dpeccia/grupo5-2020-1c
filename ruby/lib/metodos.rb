class MetodoTrait
  attr_accessor :nombre, :codigo

  def initialize(nombre_metodo, &bloque)
    @nombre = nombre_metodo
    @codigo = bloque
  end

  def es_mi_nombre? otro_nombre
    @nombre == otro_nombre
  end
end

class MetodoConflictivo
  attr_accessor :nombre, :codigo1, :codigo2

  def initialize(nombre_metodo, metodos)
    @nombre = nombre_metodo
    @codigo1 = metodos[0].codigo
    @codigo2 = metodos[1].codigo
  end
end