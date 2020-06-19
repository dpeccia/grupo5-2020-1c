trait Competidor {
  def puedeParticipar(posta: Posta): Boolean
  def tieneArma: Boolean
  def cuantoPuedeCargar: Double
  def danioQueProduce: Double
  def velocidad: Double
  def incrementarNivelDeHambre(hambreAIncrementar: Double)
  def barbarosidad: Double
  def nivelDeHambre: Double
  def esMejorQue(otroCompetidor: Competidor)(posta: Posta): Boolean = posta.esMejor(this, otroCompetidor)
}

case class Vikingo(var peso: Double,var barbarosidad: Double,var item: Item,var velocidad: Double) extends Competidor {

  var nivelDeHambre: Double = 0

  def tieneArma: Boolean = item.isInstanceOf [Arma]

  def tieneItem(unItem: Item): Boolean = item.equals(unItem)

  def cuantoPuedeCargar: Double = peso / 2 + 2 * barbarosidad

  def puedeParticipar(posta: Posta): Boolean = {
    val unCompetidor: Competidor = copy()
    unCompetidor.incrementarNivelDeHambre(posta.nivelDeHambreQueIncrementa)
    unCompetidor.nivelDeHambre < 100
  }

  def danioQueProduce: Double = barbarosidad + item.danioQueProduce

  def dragonesQuePuedeMontar(dragones: List[Dragon]): List[Jinete] = dragones.flatMap(dragon => montar(dragon))

  // TODO: refactor a la posta
  // TODO: preguntar si es necesario fijarse si puede participar de la posta aca
  def mejorMontura(dragones: List[Dragon], posta: Posta): Competidor = {
    val competidor = this.dragonesQuePuedeMontar(dragones).fold(this)((competidor, otroCompetidor) => {
      if(competidor.esMejorQue(otroCompetidor)(posta))
        competidor
      else
        otroCompetidor
    })
    competidor
  }

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit =  nivelDeHambre += hambreAIncrementar

  def montar(dragon: Dragon): Option[Jinete] = {
    if(dragon.puedeSerMontadoPor(this)) {
      return Some(Jinete(this, dragon))
    }
    None
  }

}

case class Jinete(var vikingo: Vikingo, var dragon: Dragon) extends Competidor {
  def nivelDeHambre: Double = vikingo.nivelDeHambre

  def puedeParticipar(posta: Posta): Boolean = vikingo.puedeParticipar(posta)

  def cuantoPuedeCargar: Double = vikingo.peso - dragon.cuantoPuedeCargar

  def danioQueProduce: Double = vikingo.danioQueProduce + dragon.danio

  def velocidad: Double = (dragon.velocidadDeVuelo - vikingo.peso).max(0)

  def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = vikingo.incrementarNivelDeHambre(vikingo.nivelDeHambre * 0.05)

  def barbarosidad: Double = vikingo.barbarosidad

  def tieneArma: Boolean = vikingo.tieneArma
}

class Patapez(_peso: Double,_barbarosidad: Double, _velocidad: Double,_item: Item = Comestible(10)) extends Vikingo(_peso,_barbarosidad,_item,_velocidad){
  //TODO preguntar la cantidad de calorias del comestible, por ahora le puse 10 para ponerle un numero
  override def puedeParticipar(posta: Posta): Boolean = nivelDeHambre < 50

  override def incrementarNivelDeHambre(hambreAIncrementar: Double): Unit = super.incrementarNivelDeHambre(hambreAIncrementar*2)
}
