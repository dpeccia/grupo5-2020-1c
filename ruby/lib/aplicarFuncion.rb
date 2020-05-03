class AplicarFuncion
  @metodo
  def initialize(simbolo)
    @metodo = simbolo
  end

  def aplicar(un_bloque,otro_bloque)
    nuevo_bloque = proc do |*params,&bloque|
      #[un_bloque,otro_bloque].map{|bloque| bloque.call(*params,&bloque)}.reduce(@metodo_aplicar)
        res = un_bloque.call(otro_bloque.call(*params,&bloque))
        @metodo.call(res)
      #[un_bloque,otro_bloque].inject{|res,b| res = @metodo_aplicar.call(b.call)}
    end
    nuevo_bloque
  end
end
