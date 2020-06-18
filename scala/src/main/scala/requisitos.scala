trait RequisitoDragon {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean
}

trait RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean
}

case object RequisitoPeso extends RequisitoDragon {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso < dragon.cuantoPuedeCargar
}

case class RequisitoItemParticular (item: Item) extends RequisitoDragon {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.tieneItem(item)
}

case object RequisitoDanio extends RequisitoDragon {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = dragon.danio > vikingo.danioQueProduce
}

case class RequisitoArma() extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.tieneArma
}

case class RequisitoMontura() extends RequisitoPosta {
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.isInstanceOf [Jinete]
}

case class RequisitoBarbarosidad (minimaBarbarosidad: Int) extends RequisitoDragon with RequisitoPosta {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.barbarosidad >= minimaBarbarosidad
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.barbarosidad >= minimaBarbarosidad
}

case class RequisitoPesoDeterminado (pesoDeterminado: Int) extends RequisitoDragon with RequisitoPosta {
  def cumpleRequisito(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso <= pesoDeterminado
  def cumpleRequisito(competidor: Competidor): Boolean = competidor.cuantoPuedeCargar > pesoDeterminado
}