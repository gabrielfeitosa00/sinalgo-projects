/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projects.sensor.nodes.nodeImplementations;

import java.awt.Color;

import projects.sensor.nodes.messages.WsnMsg;
import projects.sensor.nodes.timers.WsnMessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class SensorNode extends Node {

    //Armazenar o no que sera usado para alcancar a Estacao-Base
    private Node proximoNoAteEstacaoBase=null;
    
    //Armazena o numero de sequencia da ultima mensagem recebida
    private Integer sequenceNumber = 0;
    
    //Armazena o nó estação baze original para direcionar a mensagem
    
    private Node estacaoBase=null;
        
    //Contador de mensagens enviadas até o sink
    private Integer msg_enviadas = 0;
    
    //Contador de rounds passados
	private int tempoRound = 0;
    @Override
	public void handleMessages(Inbox inbox) {
	 
		while (inbox.hasNext()) {
			Message message = inbox.next();

			if (message instanceof WsnMsg) {
				Boolean shouldBroadcast = Boolean.TRUE;// booleano que controla o broadcast
				
				WsnMsg wsnMessage = (WsnMsg) message;// Converter message para um objeto do tipo WsnMsg

				if (wsnMessage.tipoMsg == 0) { // Se a mengagem for do tipo 0 ela será uma mensagem de roteamento
					if (proximoNoAteEstacaoBase == null) {
				
						estacaoBase = wsnMessage.origem;
						proximoNoAteEstacaoBase = inbox.getSender();// Estabelece como proximo no ate a origem
																	// o no que enviou a mensagem
										
						this.setColor(wsnMessage.origem.getColor());
					}   else {
						if (wsnMessage.origem.ID != this.estacaoBase.ID) { // Se o ID da mensagem é diferente
																		   // do ID da estação base se define a estação
																		   // base como o no origem da mensagem e o proximo
																		   // no ate estaçao como o no que enviou a mensagem
							estacaoBase = wsnMessage.origem;
							proximoNoAteEstacaoBase = inbox.getSender();
							this.setColor(wsnMessage.origem.getColor());
						} else {
							shouldBroadcast = Boolean.FALSE;
						}
					}
				} else if (wsnMessage.tipoMsg == 1) {
					// Se a mensgem for do tipo 1 ela estara retornando ao nó Sink
					// dessa forma a mensagem deve ser enviada ate proximoNoAteEstacaoBase
			 
					shouldBroadcast = Boolean.FALSE;
					
					// previnir que este sensor envie uma mensagem ao proximo No
					// e enviar apenas para proximoNoAteEstacaoBase
				 
					if (proximoNoAteEstacaoBase != null) {
						
						this.send(wsnMessage, proximoNoAteEstacaoBase);
					
					}
				}

				// Se shouldBroadcast for TRUE, entao este No Sensor continua transmitindo a
				// Mensagem aos vizinhos
				if (shouldBroadcast) {
		 
					this.broadcast(wsnMessage);
				}
			}
		}
	}

    @Override
    public void preStep() {
		// A condicao abaixo verifica se existe um Proximo No para Estacao-Base
		// Lembrando que esse Proximo No só existira depois que este Sensor receber uma
		// msg tipo 0, ou seja, receber um pacote em broadcast
    	// A mensagem é enviada a cada round par
    	tempoRound++;
		if (proximoNoAteEstacaoBase != null&&tempoRound%2==0) {
		 
				WsnMsg wsnMessage = new WsnMsg(sequenceNumber,this, null,this, 1);
				sequenceNumber++;
				send(wsnMessage, proximoNoAteEstacaoBase);
				msg_enviadas++;
				this.setColor(Color.GREEN);
			 
		} else if(tempoRound%2!=0) {
			this.setColor(Color.YELLOW);
		}
		
 
		 
    }

 

    @Override
    public void init() {
 
		this.setColor(Color.YELLOW);
    
    }

    @Override
    public void neighborhoodChange() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStep() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
	@NodePopupMethod(menuText = "Mostrar Atributos deste No-Sensor")
	public void mostrarAtributosDesteSensor() {
		System.out.println("----\nExibindo atributos de SensorNode-"+this.ID);
		System.out.println("Proximo No ate estacao-base = " + proximoNoAteEstacaoBase);
		System.out.println("Mensagens enviadas ao no base = " + msg_enviadas);
 
	}

}
