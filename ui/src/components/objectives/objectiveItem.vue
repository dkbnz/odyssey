<template>
    <div>
        <h1 class="page-title">{{ heading }} an Objective!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on objective creation as a loading bar
        for the objective being added to the database -->
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
                                id="objective_riddle-field"
                                label="Objective Riddle:"
                                label-for="objective_riddle">
                            <b-form-textarea :type="'expandable-text'"
                                             id="objective_riddle"
                                             trim
                                             v-model="inputObjective.riddle"
                                             :state="validateRiddle"></b-form-textarea>
                        </b-form-group>
                    </b-container>


                    <b-form>
                        <b-container fluid>
                            <p class="mb-1">Selected Destination:</p>
                            <b-list-group @click="$emit('destination-select')">
                                <b-list-group-item href="#" class="flex-column align-items-start"
                                                   v-if="destinationSelected"
                                                   id="selectedDestination"
                                                   :disabled="destinationSelected === '{}'"
                                                   :variant="checkDestinationState"
                                                   draggable="false">
                                    <div class="d-flex w-100 justify-content-between">
                                        <h5 class="mb-1" v-if="destinationSelected.name">
                                            {{destinationSelected.name}}
                                        </h5>
                                        <h5 class="mb-1" v-else>Select a Destination</h5>

                                    </div>

                                    <p>
                                        {{destinationSelected.district}}
                                    </p>
                                    <p>
                                        {{destinationSelected.country}}
                                    </p>
                                </b-list-group-item>
                            </b-list-group>
                            <b-form-group
                                    class="mb-5"
                                    id="radius-field"
                                    label="Selected Destination check in radius"
                                    label-for="radius">
                                    <vue-slider
                                            :disabled="destinationSelected === '{}'"
                                            class="ml-3 mr-3"
                                            id="radius"
                                            v-model="radiusSelected"
                                            :marks="true"
                                            :data="radiusList"
                                            :max=10>
                                    </vue-slider>
                            </b-form-group>


                            <div ref="map" v-if="radiusSelected !== null && destinationSelected.name">
                                <google-map ref="map"
                                            :showRadius="true"
                                            :radius="radiusSelected.value"
                                            :selectedRadiusDestination="destinationSelected">
                                </google-map>
                            </div>
                            <div ref="map" v-else-if="radiusSelected == null && destinationSelected.name">
                                <google-map ref="map"
                                            :showRadius="false"
                                            :selectedRadiusDestination="destinationSelected"
                                            :destinations="[]">
                                </google-map>
                            </div>
                        </b-container>
                    </b-form>
                </b-form>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateObjective" block variant="primary">
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
        name: "addObjective",

        components: {
            BCol,
            DestinationSidebar,
            GoogleMap
        },

        props: {
            profile: Object,
            inputObjective: {
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
            heading: String
        },

        data() {
            return {
                showError: false,
                showDateError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                savingObjective: false,
                letObjectiveSaved: false,
                radiusList: [
                    {value: 0.005, text: "5 m"},
                    {value: 0.01, text: "10 m"},
                    {value: 0.02, text: "20 m"},
                    {value: 0.03, text: "30 m"},
                    {value: 0.04, text: "40 m"},
                    {value: 0.05, text: "50 m"},
                    {value: 0.1, text: "100 m"},
                    {value: 0.25, text: "250 m"},
                    {value: 0.5, text: "500 m"},
                    {value: 1, text: "1 Km"},
                    {value: 2.5, text: "2.5 Km"},
                    {value: 5, text: "5 Km"},
                    {value: 7.5, text: "7.5 Km"},
                    {value: 10, text: "10 Km"},
                ],
                initial: {
                    zoom: 5,
                    view: {
                        lat: -41.272804,
                        lng: 173.299565
                    }
                },
                destinationSelected: {},
                radiusSelected: {value: 0.005, text: "5 Metres"}
            }
        },

        watch: {
            inputObjective() {
                this.destinationSelected = this.inputObjective.destination;
            },

            selectedDestination() {
                this.destinationSelected = this.selectedDestination;
                this.inputObjective.destination = this.destinationSelected;
            }
        },

        mounted() {
            this.editingObjective();
        },

        computed: {
            /**
             * Returns true if the inputted riddle has length greater than 0.
             *
             * @returns {Boolean} true if validated.
             */
            validateRiddle() {
                if (this.inputObjective.riddle.length > 0) {
                    return true;
                } else if (this.inputObjective.riddle.length > 250) {
                    return false;
                }
                return null;
            },


            /**
             * Returns true if the user has selected a check in radius
             *
             * @returns {boolean} true if validated.
             */
            validateCheckIn() {
                if (this.radiusSelected.value === null) {
                    return false;
                }
                return this.radiusSelected.value > 0;
            },


            /**
             * Returns true if the input destination exists and matches the one selected in the sidebar and isn't empty.
             *
             * @returns {boolean} true if valid.
             */
            validateDestination() {
                return (this.inputObjective.destination !== null
                    && this.inputObjective.destination === this.destinationSelected
                    && this.inputObjective.destination.name !== undefined
                    && this.inputObjective.destination.name.length > 0
                    || this.inputObjective.destination !== null
                    && this.inputObjective.destination.id === this.destinationSelected.id);
            },


            /**
             * Checks the validity of the destination using validateDestination and returns the appropriate state for
             * display.
             *
             * @returns         {string}, 'success' if destination is valid, 'secondary' otherwise.
             */
            checkDestinationState() {
                return this.validateDestination ? "success" : "secondary"
            },
        },

        methods: {
            /**
             * Fills the destination with the existing destination of a objective when editing it.
             */
            editingObjective() {
                if (this.inputObjective.id !== null) {
                    this.destinationSelected = this.inputObjective.destination;
                    this.radiusSelected = this.radiusList[this.findIndexInArray()];
                }
            },


            /**
             * Locates the given objectives radius in the list of radius values.
             */
            findIndexInArray() {
                for (let i = 0; i < this.radiusList.length; i += 1) {
                    if (this.radiusList[i]['value'] === this.inputObjective.radius) {
                        return i;
                    }
                }
                return -1;
            },


            /**
             * If all field validations pass on the active objective, saves the objective using either
             * updateObjective if there is an active editing ID or saveObjective otherwise (adding a new one).
             */
            validateObjective() {
                if (this.validateDestination && this.validateRiddle && this.validateCheckIn) {
                    if (this.heading === "Create" || this.heading === "Add") {
                        this.addObjective();
                    } else if (this.heading === "Edit") {
                        this.editObjective();
                    } else {
                        // These fields are necessary as when editing or creating objectives in a quest behaviour needs
                        // to work differently.
                        if (this.inputObjective.id) {
                            this.updateObjective();
                        } else {
                            this.saveObjective()
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
                this.destinationSelected = {};
            },


            /**
             * When used in quests for adding a objective to the quests list by emitting it outside of the
             * component.
             */
            addObjective() {
                this.inputObjective.radius = this.radiusSelected.value;
                delete this.inputObjective.startTime;
                delete this.inputObjective.endTime;
                delete this.inputObjective.startDate;
                delete this.inputObjective.endDate;
                this.$emit('addObjective', this.inputObjective);
                this.$emit('cancelCreate')
            },


            /**
             * When used in quests for editing a objective to the quests list by emitting it outside of the
             * component.
             */
            editObjective() {
                this.inputObjective.radius = this.radiusSelected.value;
                delete this.inputObjective.startTime;
                delete this.inputObjective.endTime;
                delete this.inputObjective.startDate;
                delete this.inputObjective.endDate;
                // Emits here for editing an objective in a quest, as later the destination value is changed.
                this.$emit('editObjective', this.inputObjective);
                this.$emit('cancelCreate')
            },


            /**
             * Creates formatted Json of the currently active objective.
             */
            assembleObjective() {
                this.inputObjective.destination = {"id": this.inputObjective.destination.id};
            },


            /**
             * POST's the currently active destination to the objectives endpoint in Json format, for newly creating
             * destinations.
             */
            saveObjective() {
                this.assembleObjective();
                let self = this;
                fetch('/v1/objectives/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputObjective)
                })
                    .then(this.checkStatus)
                    .then(function (response) {
                        self.parseJSON(response).then(data => {
                            self.createPointToast(data.pointsRewarded, "Objective Created");
                        });
                        self.$emit('successCreate', "Objective Successfully Created");
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Displays a toast saying they've gained a certain amount of points.
             * @param points the points to display.
             * @param title the title of the toast, indicating the context of the point gain.
             */
            createPointToast(points, title) {
                let message = "Your points have increased by " + points;
                this.$bvToast.toast(message, {
                    title: title,
                    autoHideDelay: 3000,
                    appendToast: true
                })
            },


            /**
             * PUT's the currently active destination to the objectives endpoint in Json format, for edited
             * destinations.
             */
            updateObjective() {
                this.assembleObjective();
                let self = this;
                fetch('/v1/objectives/' + this.inputObjective.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputObjective)
                })
                    .then(this.checkStatus)
                    .then(function () {
                        self.$emit('successCreate', "Objective Successfully Edited");
                        self.$emit('cancelCreate')
                    })
            },


            /**
             * Cancels the creation or editing of a objective by emitting a value to the objectiveList.
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

                this.errorMessage = "";
                response.clone().text().then(text => {
                    text = JSON.parse(text);
                    let result = [];
                    for (let i = 0; i < text.length; i++) {
                        if (!response.ok) {
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
