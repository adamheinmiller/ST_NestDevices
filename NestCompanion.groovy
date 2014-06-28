/**
 *  Nest Companion
 *
 *  Copyright 2014 Adam Heinmiller
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Nest Companion",
    namespace: "https://github.com/adamheinmiller",
    author: "Adam Heinmiller",
    description: "Companion Smart App for Nest device types",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)


preferences 
{
	section("Nest Devices") 
    {
    	input "protects", "device.nestProtect", title: "Protects", multiple: true, required: false
    	input "thermostats", "device.nestthermostat", title: "Thermostats", multiple: true, required: false
	}
    
    section("Refresh Duration")
    {
    	input "refreshMins", "number", title: "Minutes (default 60)", required: false
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
    unschedule()
    
	initialize()
}

def initialize() 
{
	state.RefreshDuration = (refreshMins ?: 60) * 60
    
    scheduleUpdate(state.RefreshDuration)
}

def updateDevices()
{
	protects?.poll()    
    thermostats?.poll()


    scheduleUpdate(state.RefreshDuration)
}

def scheduleUpdate(duration)
{
	runIn(duration, updateDevices)

	log.debug "Scheduled Nest Update in $duration seconds"
}
