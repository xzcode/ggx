package com.ggx.core.common.handler.idle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ggx.core.common.config.GGXCoreConfig;
import com.ggx.core.common.event.GGXCoreEvents;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.session.GGXSession;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 空闲触发器
 *
 * @author zai
 * 2017-08-04 23:23:06
 */
public class IdleHandler extends ChannelInboundHandlerAdapter{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(IdleHandler.class);

	private GGXCoreConfig config;
	
	
	private boolean readerIdleEnabled;
	
	private boolean writerIdleEnabled;
	
	private boolean allIdleEnabled;
	
	public IdleHandler(GGXCoreConfig config) {
		this.config = config;
		init();
	}
	
	
	public void init() {
		checkIdleEventMapped();
	}
	
	public void checkIdleEventMapped() {
		
		if(config.getEventManager().hasEventListener(GGXCoreEvents.Idle.WRITE)) {
			this.writerIdleEnabled = true;
		}
		
		if (config.getEventManager().hasEventListener(GGXCoreEvents.Idle.READ)) {
			this.readerIdleEnabled = true;
		}
		
		if (config.getEventManager().hasEventListener(GGXCoreEvents.Idle.ALL)) {
			this.allIdleEnabled = true;
		}
		
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		
		if (evt instanceof IdleStateEvent) {
			
            switch (((IdleStateEvent) evt).state()) {
            	
				case WRITER_IDLE:
						if(writerIdleEnabled) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("...WRITER_IDLE...: channel:{}", ctx.channel());								
							}
							GGXSession session = config.getSessionFactory().getSession(ctx.channel());
							config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Idle.WRITE, null));
						}
					break;
				case READER_IDLE:
						if (readerIdleEnabled) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("...READER_IDLE...: channel:{}", ctx.channel());								
							}
							GGXSession session = config.getSessionFactory().getSession(ctx.channel());
							config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Idle.READ, null));
						}
					break;
				case ALL_IDLE:
						if (allIdleEnabled) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("...ALL_IDLE...: channel:{}", ctx.channel());								
							}
							GGXSession session = config.getSessionFactory().getSession(ctx.channel());
							config.getEventManager().emitEvent(new EventData<>(session, GGXCoreEvents.Idle.ALL, null));
						}
					break;
				default:
					break;
					
			}
            
        }  
        super.userEventTriggered(ctx, evt);  
	}
}
