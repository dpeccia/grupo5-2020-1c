require_relative '../lib/trait'

describe "Tests de Traits" do

  before :all do
    Trait.define do
      name :MiTrait
      method :metodo1 do
        p "Hola"
      end
      method :metodo2 do |un_numero|
        un_numero * 0 + 42
      end
    end

    Trait.define do
      name :OtroTrait
      method :metodo1 do
        p "Chau"
      end
      method :metodo4 do |un_numero|
        un_numero * 0 + 42
      end
      Trait.define do
        name :MiTrait2
        method :metodo1 do
          10
        end
      end

      Trait.define do
        name :OtroTrait2
        method :metodo1 do
          20
        end
      end

    end

    class MiClase
      uses MiTrait
      def metodo1
        "mundo"
      end
    end

    class A
      uses MiTrait + OtroTrait
    end

    class B
      uses MiTrait - :metodo2
    end
    condicion = proc do |numero|
      numero.even?
    end
    MiTrait2.estrategia = AplicarPorCondicion.new(&condicion)
    class C
      uses MiTrait2 + OtroTrait2
    end
    #class ConAlias
      #uses MiTrait << (:metodo1 >> :saludo)
      #end

  end

  describe 'Definicion de Trait' do
    it 'se creo el trait con su nombre' do
      expect{Object.const_get(MiTrait.nombre)}.not_to raise_exception
    end
    it 'se guardo el nombre que le definimos' do
      expect(MiTrait.nombre).to be :MiTrait
    end
    it 'tiene los metodos que le definimos' do
      expect(MiTrait.metodos.keys).to eq [:metodo1,:metodo2]
    end
    it 'el :metodo2 solo se creo para esa instancia' do
      expect(MiTrait.metodos).to include :metodo2
    end
  end

  describe 'Aplicacion de Trait' do
    it 'MiClase tiene el :metodo2 de MiTrait' do
      expect(MiClase.instance_methods).to include :metodo2
    end
    it 'Mi Clase devuelve el metodo'do
      expect(MiClase.new.metodo2 2).to eq 42
    end
    it 'no se pisa el metodo que ya tenia la clase' do
      expect(MiClase.new.metodo1).to eq "mundo"
    end
  end

  describe 'Suma de Traits' do
    it 'tiene los metodos de ambos traits' do
      expect(A.instance_methods).to include :metodo1,:metodo2,:metodo4
    end
    it 'tira error si habia metodos con el mismo nombre en los traits' do
      expect { A.new.metodo1 }.to raise_error("Metodo Repetido")
    end
  end

  describe 'Resta de Traits' do
    it 'se remueve el metodo especificado' do
      expect(B.instance_methods).not_to include :metodo2
    end
    it 'tira error si se remueve un metodo inexistente' do
      expect{class C uses MiTrait - :metodo8 end}.to raise_error "Solo remueve metodos incluidos en su trait"
    end
  end

  describe 'Renombrar selectores' do
    it 'tiene los metodos viejos y el renombrado' do
      expect(ConAlias.new).to respond_to :metodo1 and expect(ConAlias.new).to respond_to :saludo
    end
    it 'tira error si se trata de renombrar un metodo que no existe' do
      expect{class ConAlias2 uses MiTrait << (:metodo8 >> :saludo) end}.to raise_error 'Solo puede renombrar metodos incluidos en el trait'
    end
  end
  describe "Resolucion de conflictos" do #TODO ver porque cambia la interfaz de los metodos
    xit 'la estrategia de orden de aparicion aplica ambos metodos' do
      MiTrait.estrategia = OrdenDeAparicion.new
      class C
        uses MiTrait + OtroTrait
      end
      expect(C.new.metodo1).to eq "Hola\n Chau" #TODO Ver de hacer otro test
    end
    xit 'should ' do
      def sumar un_numero
        1 + un_numero
      end
      MiTrait2.estrategia = AplicarFuncion.new(:sumar)
      class C
        uses MiTrait2 + OtroTrait2
      end
      expect(C.new.metodo1).to eq 30 #TODO ver porque queda nil el metodo
    end
    xit 'corta el flujo si cumple condicion ' do
      expect(C.new.metodo1).to eq 10 #TODO ver porque queda nil el metodo
    end

  end

end