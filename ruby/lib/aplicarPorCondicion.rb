class AplicarPorCondicion
  attr_accessor :condicion
  def initialize(&condicion)
    @condicion = condicion
  end
  def aplicar(un_bloque,otro_bloque)
    nuevo_bloque = proc do |*params,&bloque|
      [un_bloque,otro_bloque].each do |b|
        res = b.call(*params,&bloque)
        if @condicion.call(res)
          return res
        end
      end
    end
    nuevo_bloque
  end
end
