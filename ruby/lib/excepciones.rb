class ConflictosSinResolver < StandardError
  def initialize
    super("Tiene conflictos sin resolver, aplique una estrategia para resolverlos")
  end
end

class MetodoRemovidoNoExiste < StandardError
  def initialize
    super("Solo se pueden remover los metodos incluidos en ese trait")
  end
end

class MetodoRenombradoNoExiste < StandardError
  def initialize
    super("Solo se pueden renombrar los metodos incluidos en ese trait")
  end
end