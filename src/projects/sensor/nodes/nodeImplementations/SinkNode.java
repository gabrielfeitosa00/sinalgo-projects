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

public class SinkNode extends Node {

    //Armazenar o no que sera usado para alcancar a Estacao-Base
    private Node proximoNoAteEstacaoBase;
    
    //Armazena o numero de sequencia da ultima mensagem recebida
    private Integer sequenceNumber = 0;

    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message message = inbox.next();
            if (message instanceof WsnMsg) {
                WsnMsg wsnMessage = (WsnMsg) message;
                // Se entrar na condicao abaixo, isso significa que a Mensagem voltou
                // para o SinkNode que originalmente envio a Mensagem aos Nos Sensors
                // Em seguida é printado uma mensagem de confirmação  da chegada da mensagem 
                // no console
                if (wsnMessage.tipoMsg == 1) {
                    System.out.println("Sink-" + this.ID + " recebe mensagem " + wsnMessage.sequenceID + " de Node-" + wsnMessage.origem.ID);
                }
            }
        }
    }

    @Override
    public void preStep() {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @NodePopupMethod(menuText = "Construir Arvore de Roteamento")
    public void construirRoteamento() {
        this.proximoNoAteEstacaoBase = this;
        WsnMsg wsnMessage = new WsnMsg(1, this, null, this, 0);
        WsnMessageTimer timer = new WsnMessageTimer(wsnMessage);
        timer.startRelative(1, this);
        
    }

    @Override
    public void init() {
    	this.setColor(Color.BLUE);
    
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

}
