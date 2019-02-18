package com.sample;

public class JmxJdbcDomain {

	private String name;
	private String schemaName;
	private int activeConnectionsCurrentCount;
	private int connectionsTotalCount;
	private int currCapacity;
	private String state;
	private int highestNumAvailable;
	private int leakedConnectionCount;
	private int connectionDelayTime;
	private String type;
	private int  activeConnectionsAverageCount;
	private int waitSecondsHighCount;
	private int waitingForConnectionCurrentCount;
	private int waitingForConnectionHighCount;
	private String usagesDate;
	private String message;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getActiveConnectionsCurrentCount() {
		return activeConnectionsCurrentCount;
	}
	public void setActiveConnectionsCurrentCount(int activeConnectionsCurrentCount) {
		this.activeConnectionsCurrentCount = activeConnectionsCurrentCount;
	}
	public int getConnectionsTotalCount() {
		return connectionsTotalCount;
	}
	public void setConnectionsTotalCount(int connectionsTotalCount) {
		this.connectionsTotalCount = connectionsTotalCount;
	}
	public int getCurrCapacity() {
		return currCapacity;
	}
	public void setCurrCapacity(int currCapacity) {
		this.currCapacity = currCapacity;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getHighestNumAvailable() {
		return highestNumAvailable;
	}
	public void setHighestNumAvailable(int highestNumAvailable) {
		this.highestNumAvailable = highestNumAvailable;
	}
	public int getLeakedConnectionCount() {
		return leakedConnectionCount;
	}
	public void setLeakedConnectionCount(int leakedConnectionCount) {
		this.leakedConnectionCount = leakedConnectionCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getActiveConnectionsAverageCount() {
		return activeConnectionsAverageCount;
	}
	public void setActiveConnectionsAverageCount(
			int activeConnectionsAverageCount) {
		this.activeConnectionsAverageCount = activeConnectionsAverageCount;
	}
	public int getWaitSecondsHighCount() {
		return waitSecondsHighCount;
	}
	public void setWaitSecondsHighCount(int waitSecondsHighCount) {
		this.waitSecondsHighCount = waitSecondsHighCount;
	}
	public int getWaitingForConnectionCurrentCount() {
		return waitingForConnectionCurrentCount;
	}
	public void setWaitingForConnectionCurrentCount(
			int waitingForConnectionCurrentCount) {
		this.waitingForConnectionCurrentCount = waitingForConnectionCurrentCount;
	}
	public int getWaitingForConnectionHighCount() {
		return waitingForConnectionHighCount;
	}
	public void setWaitingForConnectionHighCount(
			int waitingForConnectionHighCount) {
		this.waitingForConnectionHighCount = waitingForConnectionHighCount;
	}
	public String getUsagesDate() {
		return usagesDate;
	}
	public void setUsagesDate(String usagesDate) {
		this.usagesDate = usagesDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "JmxJdbcDomain [name=" + name
				+ ", activeConnectionsCurrentCount="
				+ activeConnectionsCurrentCount + ", connectionsTotalCount="
				+ connectionsTotalCount + ", currCapacity=" + currCapacity
				+ ", state=" + state + ", highestNumAvailable="
				+ highestNumAvailable + ", leakedConnectionCount="
				+ leakedConnectionCount + ", type=" + type
				+ ", activeConnectionsAverageCount="
				+ activeConnectionsAverageCount + ", waitSecondsHighCount="
				+ waitSecondsHighCount + ", waitingForConnectionCurrentCount="
				+ waitingForConnectionCurrentCount
				+ ", waitingForConnectionHighCount="
				+ waitingForConnectionHighCount + ", usagesDate=" + usagesDate
				+ ", message=" + message +", connectionDelayTime=" + connectionDelayTime + "]";
	}
	public int getConnectionDelayTime() {
		return connectionDelayTime;
	}
	public void setConnectionDelayTime(int connectionDelayTime) {
		this.connectionDelayTime = connectionDelayTime;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
}
