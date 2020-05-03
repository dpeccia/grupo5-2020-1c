class OrdenDeAparicion
  def initialize
    super
  end
  def aplicar(un_bloque,otro_bloque)
    nuevo_bloque = proc do |*params,&bloque|
      un_bloque.call(*params,&bloque)
      otro_bloque.call(*params,&bloque)
    end
    nuevo_bloque
  end
end
