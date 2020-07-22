package com.files.movilidad

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Kotlin : AppCompatActivity() {
        lateinit var ventanas: CharArray
        var txtResultadoVentana: TextView? = null
        var txtDetalleVentana: TextView? = null
        var txtGanadores: TextView? = null
        var txtGanadores2: TextView? = null
        var btnJugar: Button? = null
        var abrirVentana: Switch? = null
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                txtResultadoVentana = findViewById(R.id.txtVentanas)
                txtDetalleVentana = findViewById(R.id.txtDetalleVentana)
                txtGanadores = findViewById(R.id.txtGanadores)
                txtGanadores2 = findViewById(R.id.txtGanadores2)
                btnJugar = findViewById(R.id.btnJugar)
                abrirVentana = findViewById(R.id.swAbrir)
                ventanas = CharArray(64)
                iniciarVentanas('C')
        }

        /**
         * Cambia el estado de las ventanas
         * @param estadoVentana
         */
        fun iniciarVentanas(estadoVentana: Char) {
                for (i in ventanas.indices) {
                        ventanas[i] = estadoVentana
                }
        }

        /**
         * Cambia el estado de las ventanas, si estan abiertas las cierra y si estan cerradas las abre.
         * @param view
         */
        fun estadoVentanas(view: View?) {
                if (abrirVentana!!.isChecked) {
                        iniciarVentanas('A')
                } else {
                        iniciarVentanas('C')
                }
                btnJugar!!.isClickable = true
        }

        /**
         * Calcula los ganadores que tienen en su numero la ventana abierta y las ventanas anterior y
         * posterior están cerradas
         * @return lista de ganadores
         */
        val ganadores: List<Int>
                get() {
                        val ganadores = ArrayList<Int>()
                        for (i in ventanas.indices) {
                                if (i == 0) {
                                        if (ventanas[i] == 'A' && ventanas[i + 1] == 'C' && ventanas[ventanas.size - 1] == 'C') {
                                                ganadores.add(i + 1)
                                        }
                                } else if (i == ventanas.size - 1) {
                                        if (ventanas[i] == 'A' && ventanas[i - 1] == 'C' && ventanas[0] == 'C') {
                                                ganadores.add(i + 1)
                                        }
                                } else {
                                        if (ventanas[i] == 'A' && ventanas[i + 1] == 'C' && ventanas[i - 1] == 'C') {
                                                ganadores.add(i + 1)
                                        }
                                }
                        }
                        return ganadores
                }

        /**
         * Calcula la lista de ganadores que tienen en su numero la ventana abierta
         * @return lista de ganadores
         */
        val ganadores2: List<Int>
                get() {
                        val ganadores = ArrayList<Int>()
                        for (i in ventanas.indices) {
                                if (ventanas[i] == 'A') {
                                        ganadores.add(i + 1)
                                }
                        }
                        return ganadores
                }

        /**
         * Muestra el estado de las ventanas despues de pasar todos los visitantes
         * Muestra los ganadores con la regla 1 y regla 2
         * Calcula cuantas ventanas hay por cada estado
         * @param view
         */
        fun mostrarresultado(view: View?) {
                calcularVentana()
                val ganadores = ganadores
                val ganadores2 = ganadores2
                var resultado = ""
                var cerrada = 0
                var abierta = 0
                var alaIzq = 0
                var alaDer = 0
                for (i in ventanas.indices) {
                        if (i % 3 == 0) resultado += "\n"
                        resultado += "Ventana " + (i + 1) + ": " + ventanas[i].toString() + " , "
                        when (ventanas[i]) {
                                'C' -> cerrada++
                                'A' -> abierta++
                                'I' -> alaIzq++
                                'D' -> alaDer++
                        }
                }
                val detalleVentana = "\n\n Ventanas abiertas = $abierta\n Ventanas cerradas = $cerrada\n Ventanas ala izquierda = $alaIzq\n Ventanas ala derecha = $alaDer"
                txtResultadoVentana!!.text = resultado
                txtDetalleVentana!!.text = detalleVentana
                txtGanadores!!.text = "Ganadores regla 1 $ganadores Total: ${ganadores.size} "
                txtGanadores2!!.text = "Ganadores regla 2 $ganadores2 Total: ${ganadores2.size}"
                btnJugar!!.isClickable = false
        }

        /**
         * Pasan todos los visitantes por las ventanas de forma ascendente.
         * @return el estado  ventanas.
         */
        fun calcularVentana(): CharArray {
                var visitante = 1
                val newVentana = ventanas
                for (i in newVentana.indices) {
                        if (visitante == 1) {
                                visitante1()
                        } else if (visitante == 2) {
                                visitante2()
                        } else if (visitante == 3) {
                                visitante3()
                        } else if (i == newVentana.size - 1) {
                                visitanteFinal()
                        } else if (visitante == 4) {
                                visitante4()
                                visitante = 0
                        }
                        visitante++
                }
                return newVentana
        }

        /**
         * Visitante 1 abre las alas izquierdas de la ventana
         * si la ventana esta cerrada abre el ala y queda el estado de la ventana en 'I'
         * si la ventana tiene el ala derecha abierta y el visitante abre el ala izquierda, la ventana queda abierta = 'A'
         * en caso contrario el visitante no hace nada porque el ala izquierda esta abierta.
         */
        fun visitante1() {
                for (i in ventanas.indices) {
                        if (ventanas[i] == 'C') {
                                ventanas[i] = 'I'
                        } else if (ventanas[i] == 'D') {
                                ventanas[i] = 'A'
                        }
                }
        }

        /**
         * Visitante 2 abre las alas derechas de las ventanas donde el numero de la ventana es par
         * Si la ventana es par y se encuentra cerrada abrir ala derecha = 'D'
         * Si la ventana es par y si la ventana esta ala izquierda abierta y el visitante abre el ala derecha, la ventana queda abierta = 'A'
         */
        fun visitante2() {
                for (i in ventanas.indices) {
                        val numeroVentana = i + 1
                        val par = numeroVentana % 2 == 0
                        if (par) {
                                if (ventanas[i] == 'C') {
                                        ventanas[i] = 'D'
                                } else if (ventanas[i] == 'I') {
                                        ventanas[i] = 'A'
                                }
                        }
                }
        }

        /**
         * Visitante 3 abre las alas izquierdas cerradas donde el numero de la ventana es múltiplo de 3,
         * y cierra las alas derechas abiertas donde el numero de la ventana es múltiplo de 3.
         * si esta la ventana cerrada abrir la izq=I
         * si la ventana tiene ala derecha abierta (D), abrir ala izquierda y cerrar ala derecha = 'I'
         * si esta la ventana abierta cerrar ala der entonces quedaria solo ala izquierda = I
         */
        fun visitante3() {
                for (i in ventanas.indices) {
                        val numeroVentana = i + 1
                        val multiplo = numeroVentana % 3 == 0
                        if (multiplo) {
                                if (ventanas[i] == 'C') {
                                        ventanas[i] = 'I'
                                } else if (ventanas[i] == 'D') {
                                        ventanas[i] = 'I'
                                } else if (ventanas[i] == 'A') {
                                        ventanas[i] = 'I'
                                }
                        }
                }
        }

        /**
         * Visitante 4 abre las alas derechas cerradas donde el numero de la ventana es múltiplo de 4,
         * y cierra las alas izquierdas abiertas donde el numero de la ventana es múltiplo de 4
         * si esta la ventana cerrada abrir la der=D
         * si esta la ventana abierta cerrar ala izquierda entonces quedaria solo ala derecha = D
         * si la ventana tiene ala izquierda abierta (I), abrir ala derecha y cerrar ala izquierda = 'D'
         */
        fun visitante4() {
                for (i in ventanas.indices) {
                        val numeroVentana = i + 1
                        val multiplo = numeroVentana % 4 == 0
                        if (multiplo) {
                                if (ventanas[i] == 'C') {
                                        ventanas[i] = 'D'
                                } else if (ventanas[i] == 'A') {
                                        ventanas[i] = 'D'
                                } else if (ventanas[i] == 'I') {
                                        ventanas[i] = 'D'
                                }
                        }
                }
        }

        /**
         * Visitante final abre el ala derecha de la ventana, si esta cerrada y la cierra si esta abierta
         * Si ventana cerrda entonces abrir ala derecha = D
         * Si ventana abierta entonces cerrar ala derecha = I
         * Si ventana ala izquierda abierta entonces abrir derecha y ventana queda abierta = A
         * Si ventana ala derecha abierta entonces cerrar ala derecha y ventana queda cerrada = C
         */
        fun visitanteFinal() {
                for (i in ventanas.indices) {
                        if (ventanas[i] == 'C') {
                                ventanas[i] = 'D'
                        } else if (ventanas[i] == 'A') {
                                ventanas[i] = 'I'
                        } else if (ventanas[i] == 'I') {
                                ventanas[i] = 'A'
                        } else if (ventanas[i] == 'D') {
                                ventanas[i] = 'C'
                        }
                }
        }
}