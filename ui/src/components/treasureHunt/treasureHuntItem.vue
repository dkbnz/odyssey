<template>

    <div>
        {{inputTreasureHunt}}

        <h1 class="page-title">{{ heading }} a Treasure Hunt!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on treasure hunt creation as a loading bar
        for the treasure hunt being added to the database -->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Trip Successfully Saved</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <b-row>
            <b-col>
                    <b-form>
                        <b-container fluid>
                            <b-form-group
                                    id="treasure_hunt_riddle-field"
                                    label="Treasure Hunt Riddle:"
                                    label-for="treasure_hunt_riddle">
                                <b-form-textarea :type="'expandable-text'"
                                                 id="treasure_hunt_riddle"
                                                 trim
                                                 v-model="inputTreasureHunt.riddle"
                                                 :state="validateRiddle"></b-form-textarea>
                            </b-form-group>
                        </b-container>


                        <b-form>
                            <b-container fluid>
                                <h6 class="mb-1">Selected Destination:</h6>
                                <b-list-group @click="$emit('destination-select')">
                                    <b-list-group-item href="#" class="flex-column align-items-start"
                                                       v-if="selectedDestination"
                                                       id="selectedDestination"
                                                       :disabled="selectedDestination.length === '{}'"
                                                       :variant="checkDestinationState"
                                                       draggable="false">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h5 class="mb-1" v-if="selectedDestination.name">
                                                {{selectedDestination.name}}
                                            </h5>
                                            <h5 class="mb-1" v-else>Select a Destination</h5>

                                        </div>

                                        <p>
                                            {{selectedDestination.district}}
                                        </p>
                                        <p>
                                            {{selectedDestination.country}}
                                        </p>
                                    </b-list-group-item>
                                </b-list-group>
                                <b-form-group
                                        id="radius-field"
                                        label="Selected Destination check in radius:"
                                        label-for="radius">
                                    <!--Dropdown field for destination check in values-->
                                    <b-form-select id="radius" trim v-model="inputTreasureHunt.radius">
                                        <option :value="radius" v-for="radius in radiusList"
                                                :state="validateCheckIn">
                                            {{radius.text}}
                                        </option>
                                    </b-form-select>
                                </b-form-group>

                                <div ref="map" v-if="inputTreasureHunt.radius !== null && selectedDestination.name">
                                    <google-map ref="map"
                                                :showRadius="true"
                                                :radius="inputTreasureHunt.radius.value"
                                                :selectedRadiusDestination="selectedDestination">
                                    </google-map>
                                </div>
                            </b-container>
                        </b-form>
                    </b-form>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateTreasureHunt" block variant="primary">
                            {{ heading }}
                        </b-button>
                    </b-col>
                    <b-col>
                        <b-button @click="cancelCreate" block>
                            Cancel
                        </b-button>
                    </b-col>
                </b-row>
            </b-col>
        </b-row>

    </div>
</template>

<script>
    import DestinationSidebar from "../destinations/destinationSidebar";
    import BCol from "bootstrap-vue/es/components/layout/col";
    import GoogleMap from "../map/googleMap";

    export default {
        name: "addTreasureHunt",

        components: {
            BCol,
            DestinationSidebar,
            GoogleMap
        },

        props: {
            profile: Object,
            inputTreasureHunt: {
                default: function () {
                    return {
                        id: null,
                        destination: null,
                        riddle: "",
                        radius: null
                    }
                }
            },
            newDestination: Object,
            selectedDestination: {},
            heading: String,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },

        data() {
            return {
                showError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                savingTreasureHunt: false,
                letTreasureHuntSaved: false,
                radiusList: [
                    {value: 0.005, text: "5 Meters"},
                    {value: 0.01, text: "10 Meters"},
                    {value: 0.02, text: "20 Meters"},
                    {value: 0.05, text: "50 Meters"},
                    {value: 0.1, text: "100 Meters"},
                    {value: 0.5, text: "500 Meters"},
                    {value: 1, text: "1 Km"},
                    {value: 5, text: "5 Km"},
                    {value: 10, text: "10 Km"},
                ],
                initial: {
                    zoom: 5,
                    view: {
                        lat: -41.272804,
                        lng: 173.299565
                    }
                },
            }
        },

        watch: {
            selectedDestination() {
                this.inputTreasureHunt.destination = this.selectedDestination;
            },

            inputTreasureHunt() {
                this.selectedDestination = this.inputTreasureHunt.destination;
                if(this.inputTreasureHunt.radius === undefined) {
                    this.inputTreasureHunt.radius = {value:0.005, text:"5 Meters"}
                }
            }
        },

        mounted() {
            this.editingTreasureHunt();
            this.initMap();
        },

        computed: {
            /**
             * Returns true if the inputted riddle has length greater than 0.
             * @returns true if validated.
             */
            validateRiddle() {
              if(this.inputTreasureHunt.riddle.length > 0){
                  return true;
              }
              return null;
            },

            /**
             * Returns true if the user has selected a check in radius
             * @returns true if validated.
             */
            validateCheckIn() {
                if (this.inputTreasureHunt.radius === null) {
                    return false;
                }
                return this.inputTreasureHunt.radius.length > 0 || this.inputTreasureHunt.radius !== null;
            },


            /**
             * Returns true if the input destination exists and matches the one selected in the sidebar and isn't empty.
             * @returns true if valid.
             */
            validateDestination() {
                if (this.inputTreasureHunt.destination !== null
                    && this.inputTreasureHunt.destination === this.selectedDestination
                    && this.inputTreasureHunt.destination.name !== undefined
                    && this.inputTreasureHunt.destination.name.length > 0
                    || this.inputTreasureHunt.destination !== null
                    && this.inputTreasureHunt.destination.id === this.selectedDestination.id) {
                    return true;
                }
                return false;
            },


            /**
             * Checks the validity of the destination using validateDestination and returns the appropriate state for
             * display.
             *
             * @returns 'success' if destination is valid, 'secondary' otherwise.
             */
            checkDestinationState(){
                return this.validateDestination ? "success" : "secondary"
            },
        },

        methods: {
            /**
             * Fills the destination with the existing destination of a hunt when editing it.
             */
            editingTreasureHunt() {
                if (this.inputTreasureHunt.id !== null) {
                    this.selectedDestination = this.inputTreasureHunt.destination;
                }
            },


            /**
             * If all field validations pass on the active treasure hunt, saves the treasure hunt using either
             * updateHunt if there is an active editing ID or saveHunt otherwise (adding a new one).
             */
            validateTreasureHunt() {
                if (this.validateDestination && this.validateRiddle && this.validateCheckIn) {
                    if (this.heading === "Add") {
                        this.addHunt();
                    } else if (this.heading === "Edit") {
                        this.editHunt();
                    } else {
                        if (this.inputTreasureHunt.id !== null) {
                            this.updateHunt();
                        } else {
                            this.saveHunt();
                        }
                    }
                } else {
                    this.errorMessage = "Not all fields have valid information!";
                    this.showError = true;
                }

            },


            /**
             * Used after the destination is added, resets the form for adding a destination.
             */
            resetDestForm() {
                this.selectedDestination = {};
            },


            /**
             * When used in quests for adding a treasure hunt to the quests list by emitting it outside of the
             * component.
             */
            addHunt() {
                this.inputTreasureHunt.radius = this.inputTreasureHunt.radius.value;
                delete this.inputTreasureHunt.startTime;
                delete this.inputTreasureHunt.endTime;
                delete this.inputTreasureHunt.startDate;
                delete this.inputTreasureHunt.endDate;
                this.$emit('addTreasureHunt', this.inputTreasureHunt);
                this.$emit('cancelCreate')
            },


            /**
             * When used in quests for editing a treasure hunt to the quests list by emitting it outside of the
             * component.
             */
            editHunt() {
                this.inputTreasureHunt.radius = this.inputTreasureHunt.radius.value;
                delete this.inputTreasureHunt.startTime;
                delete this.inputTreasureHunt.endTime;
                delete this.inputTreasureHunt.startDate;
                delete this.inputTreasureHunt.endDate;
                this.$emit('editTreasureHunt', this.inputTreasureHunt);
                this.$emit('cancelCreate')
            },


            /**
             * Creates formatted JSON of the currently active treasure hunt.
             * @returns JSON string with fields 'riddle', 'destination_id', 'start_date', 'end_date'.
             */
            assembleTreasureHunt() {
                this.inputTreasureHunt.destination = {"id": this.inputTreasureHunt.destination.id};
            },


            /**
             * POST's the currently active destination to the treasureHunts endpoint in JSON format, for newly creating
             * destinations.
             */
            saveHunt() {
                this.assembleTreasureHunt();
                let self = this;
                fetch('/v1/treasureHunts/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputTreasureHunt)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('successCreate', "Treasure Hunt Successfully Created");
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * PUT's the currently active destination to the treasureHunts endpoint in JSON format, for edited
             * destinations.
             */
            updateHunt() {
                this.assembleTreasureHunt();
                let self = this;
                fetch('/v1/treasureHunts/' + this.inputTreasureHunt.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputTreasureHunt)
                })
                    .then(this.checkStatus)
                    .then(function() {
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Cancels the creation or editing of a treasure hunt by emitting a value to the treasureHuntList.
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * Displays the countdown alert on the successful saving of a trip.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a trip.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Checks the Http response for errors.
             *
             * @param response the retrieved Http response.
             * @returns {*} throws the Http response error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error);

                this.errorMessage = "";
                response.clone().text().then(text => {
                    text = JSON.parse(text);
                    let result = [];
                    for (let i = 0; i < text.length; i++) {
                        if (!response.ok) {
                            console.log(text);
                            result.push(text[i].message);
                        }
                        else {
                            result.push(text[i]);
                        }
                    }
                    this.errorMessage = result;

                    this.showError = true;
                });
                throw error;
            },


            /**
             * Converts the retrieved Http response to a Json format.
             *
             * @param response the Http response.
             * @returns the Http response body as Json.
             */
            parseJSON(response) {
                return response.json();
            }

        }
    }
</script>
