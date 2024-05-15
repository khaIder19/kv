/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kv.notif.dto;


/**
 *
 * @author k-iderr
 */
public class BaseDaoEventDto {
        private long time;
        private String eventType;
        private String payloadType;
        private String crudType;
        private Object payload;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public String getPayloadType() {
            return payloadType;
        }

        public void setPayloadType(String payloadType) {
            this.payloadType = payloadType;
        }

        public String getCrudType() {
            return crudType;
        }

        public void setCrudType(String crudType) {
            this.crudType = crudType;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }
            
}
