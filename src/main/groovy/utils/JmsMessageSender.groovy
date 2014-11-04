package utils

import groovy.util.logging.Slf4j

import javax.jms.Connection
import javax.jms.ConnectionFactory
import javax.jms.DeliveryMode
import javax.jms.Destination
import javax.jms.JMSException
import javax.jms.MessageProducer
import javax.jms.Session
import javax.jms.TextMessage

@Slf4j
class JmsMessageSender implements MessageSender {

    private final Connection connection
    private final Destination destination

    public JmsMessageSender(ConnectionFactory connectionFactory, Destination destination) throws JMSException {
        connection = connectionFactory.createConnection()
        connection.start()
        this.destination = destination
    }

    @Override
    public void send(String messageText) {
        boolean transacted = false
        try {
            Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE)
            MessageProducer producer = session.createProducer(destination)
            producer.deliveryMode = DeliveryMode.NON_PERSISTENT
            TextMessage message = session.createTextMessage()
            message.text = messageText
            producer.send(message)
            producer.close()
            session.close()
        } catch (JMSException cause) {
            log.debug(cause)
            cause.printStackTrace()
        }
    }
}
