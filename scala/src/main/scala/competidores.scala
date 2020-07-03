trait Competidor {
  def puedeParticipar(posta: Posta): Boolean
  def tieneArma: Boolean
  def cuantoPuedeCargar: Double
  def danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double): Competidor
  def barbarosidad: Double
  def nivelDeHambre: Double
  def esMejorQue(otroCompetidor: Competidor)(posta: Posta): Boolean = posta.esMejor(this, otroCompetidor)
}

case class Vikingo(peso: Double, barbarosidad: Double, item: Item, velocidad: Double, nivelDeHambre: Double = 0) extends Competidor {

  def tieneArma: Boolean = item match {
    case Arma(_) => true
    case _ => false
  }

  def tieneItem(unItem: Item): Boolean = item.equals(unItem)

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedeParticipar(posta: Posta): Boolean = {
    val unCompetidor: Competidor = this.incrementarNivelDeHambre(posta.nivelDeHambreQueIncrementa)
    unCompetidor.nivelDeHambre < 100
  }

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def dragonesQuePuedeMontar(dragones: List[Dragon]): List[Competidor] = dragones.flatMap(dragon => montar(dragon))

  def mejorCompetidor(dragones: List[Dragon], posta: Posta): Competidor = this.dragonesQuePuedeMontar(dragones).maxBy(esMejorQue (_)(posta))

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Vikingo = copy(nivelDeHambre = nivelDeHambre + hambreAIncrementar)

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(Jinete(this, dragon))
    }
    None
  }

}

case class Jinete(vikingo: Vikingo, dragon: Dragon) extends Competidor {
  def nivelDeHambre: Double = vikingo.nivelDeHambre

  def puedeParticipar(posta: Posta): Boolean = vikingo.puedeParticipar(posta)

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Jinete = copy(vikingo = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05))

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma
}

case object Patapez extends Vikingo(10,10, Comestible(10), 10) {
  
  override def puedeParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Vikingo = super.incrementarNivelDeHambre(hambreAIncrementar*2)
}
