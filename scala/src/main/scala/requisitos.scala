// Requisitos del dragon

trait RequisitoDragon extends ((Dragon, Vikingo) => Boolean)

case object RequisitoPeso extends RequisitoDragon {
  def apply(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso < dragon.cuantoPuedeCargar
}

case class RequisitoItemParticular (item: Item) extends RequisitoDragon {
  def apply(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.tieneItem(item)
}

case object RequisitoDanio extends RequisitoDragon {
  def apply(dragon: Dragon, vikingo: Vikingo): Boolean = dragon.danio > vikingo.danioQueProduce
}

case class RequisitoBarbarosidadDragon (minimaBarbarosidad: Int) extends RequisitoDragon {
  def apply(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.barbarosidad >= minimaBarbarosidad
}

case class RequisitoPesoDeterminadoDragon (pesoDeterminado: Int) extends RequisitoDragon {
  def apply(dragon: Dragon, vikingo: Vikingo): Boolean = vikingo.peso <= pesoDeterminado
}

// Requisitos de la posta

trait RequisitoPosta extends (Competidor => Boolean)

case class RequisitoArma() extends RequisitoPosta {
  def apply(competidor: Competidor): Boolean = competidor.tieneArma
}

case class RequisitoMontura() extends RequisitoPosta {
  def apply(competidor: Competidor): Boolean = competidor.isInstanceOf [Jinete]
}

case class RequisitoBarbarosidadPosta (minimaBarbarosidad: Int) extends RequisitoPosta {
  def apply(competidor: Competidor): Boolean = competidor.barbarosidad >= minimaBarbarosidad
}

case class RequisitoPesoDeterminadoPosta (pesoDeterminado: Int) extends RequisitoPosta {
  def apply(competidor: Competidor): Boolean = competidor.cuantoPuedeCargar > pesoDeterminado
}