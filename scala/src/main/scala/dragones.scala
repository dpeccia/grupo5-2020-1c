trait Dragon {
  val peso: Int
  val danio: Int
  val requisitosEspecificos: Set[RequisitoDragon]
  val requisitos: Set[RequisitoDragon] = requisitoBase ++ requisitosEspecificos

  def requisitoBase: Set[RequisitoDragon] = Set(RequisitoPeso)

  def velocidadBase: Double = 60

  def velocidadDeVuelo: Double = velocidadBase - peso

  def cuantoPuedeCargar: Double = 0.2 * peso

  def puedeSerMontadoPor(vikingo: Vikingo): Boolean = requisitos.forall(_.apply(this, vikingo))
}

case class FuriaNocturna (peso: Int,danio: Int, item: Item) extends Dragon() {
  override val requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoItemParticular(item))
  override def velocidadDeVuelo: Double = super.velocidadDeVuelo * 3
}

case class NadderMortifero (peso: Int) extends Dragon() {
  override val requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoDanio)
  override val danio: Int = 150
}

case class Gronckle (peso: Int, pesoDeterminado: Int) extends Dragon() {
  override val requisitosEspecificos: Set[RequisitoDragon] = Set(RequisitoPesoDeterminadoDragon(pesoDeterminado))
  override val danio: Int = peso * 5
  override def velocidadBase: Double = super.velocidadBase / 2
}


