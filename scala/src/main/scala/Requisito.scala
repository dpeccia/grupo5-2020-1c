trait Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean
}

object RequisitoPeso extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso < dragon.cuantoPuedeCargar
}

class RequisitoBarbarosidad (minimaBarbarosidad: Int) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.barbarosidad >= minimaBarbarosidad
}

class RequisitoItemParticular (item: Item) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.item.equals(item)
}

object RequisitoDanio extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = dragon.danio > vikingo.danioQueProduce
}

class RequisitoPesoDeterminado (pesoDeterminado: Int) extends Requisito {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso <= pesoDeterminado
}
