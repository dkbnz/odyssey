<template>

    <div>

        <h1 class="page-title">{{ heading }} a Treasure Hunt!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on trip creation as a loading bar
        for the trip being added to the database -->
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
                                              v-model="inputTreasureHunt.riddle"></b-form-textarea>
                            </b-form-group>
                        </b-container>


                        <b-form @reset="resetDestForm">
                            <b-container fluid>
                                <b-row>
                                    <b-col>
                                        <h6 class="mb-1">Selected Destination:</h6>
                                        <b-list-group>
                                            <b-list-group-item href="#" class="flex-column align-items-start"
                                                               v-if="destination"
                                                               id="selectedDestination"
                                                               :disabled="destination.length === '{}'">
                                                <div class="d-flex w-100 justify-content-between">
                                                    <h5 class="mb-1" v-if="destination.name">
                                                        {{destination.name}}
                                                    </h5>
                                                    <h5 class="mb-1" v-else>Select a Destination</h5>

                                                </div>

                                                <p>
                                                    {{destination.district}}
                                                </p>
                                                <p>
                                                    {{destination.country}}
                                                </p>
                                            </b-list-group-item>
                                        </b-list-group>
                                    </b-col>

                                    <b-col>
                                        <b-form-group
                                                id="startDate-field"
                                                label="Start Date:"
                                                label-for="startDate">
                                            <b-row>
                                            <b-col cols="6">
                                                <b-form-input :type="'date'"
                                                              id="startDate"
                                                              min='getCurrentDate()'
                                                              max='9999-12-31'
                                                              trim
                                                              v-model="inputTreasureHunt.startDate"
                                                              :state="validateStartDate">

                                                </b-form-input>
                                            </b-col>

                                            <b-col cols="6">
                                                <b-form-input :type="'time'"
                                                              id="startTime"
                                                              min='getCurrentTime()'
                                                              max=''
                                                              trim
                                                              v-model="inputTreasureHunt.startTime"
                                                              :state="validateStartDate">
                                                </b-form-input>
                                            </b-col>
                                            </b-row>

                                        </b-form-group>
                                        <b-form-group
                                                id="endDate-field"
                                                label="Expiration Date:"
                                                label-for="endDate">
                                            <b-col cols="6"></b-col>
                                            <b-col cols="6"></b-col>

                                            <b-row>
                                                <b-col cols="6">
                                                    <b-form-input :type="'date'"
                                                                  id="endDate"
                                                                  min='getCurrentDate()'
                                                                  max='9999-12-31'
                                                                  trim
                                                                  v-model="inputTreasureHunt.endDate"
                                                                  :state="validateEndDate">

                                                    </b-form-input>
                                                </b-col>

                                                <b-col cols="6">
                                                    <b-form-input :type="'time'"
                                                                  id="endTime"
                                                                  min='getCurrentTime()'
                                                                  max=''
                                                                  trim
                                                                  v-model="inputTreasureHunt.endTime"
                                                                  :state="validateEndDate">
                                                    </b-form-input>
                                                </b-col>
                                            </b-row>
                                        </b-form-group>
                                    </b-col>


                                </b-row>

                                <!--<b-button @click="checkDestination"-->
                                <!--class="mr-2 float-right"-->
                                <!--variant="primary">-->
                                <!--Add Destination-->
                                <!--</b-button>-->

                            </b-container>
                        </b-form>
                    </b-form>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateTreasureHunt" block variant="primary">
                            Save
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
    export default {
        name: "addTreasureHunt",

        components: {
            BCol,
            DestinationSidebar
        },

        props: {
            profile: Object,
            inputTreasureHunt: {
                default: function () {
                    return {
                        id: null,
                        riddle: "",
                        startDate: "",
                        endDate: "",
                    }
                }
            },
            heading: String,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },

        data() {
            return {
                destination: {},
                startDate: "",
                startTime: "",
                endDate: "",
                endTime: "",
                showError: false,
                showDateError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                savingTreasureHunt: false,
                letTreasureHuntSaved: false,
                selectedDestination: {}
            }
        },

        mounted() {
            this.getTreasureHuntDestination(destinationSolution => this.destination = destinationSolution);
        },

        computed: {
            validateStartDate() {

            },

            validateEndDate() {

            }
        },


        methods: {

            resetDestForm() {

            },


            validateTreasureHunt() {
                this.getTreasureHuntDestination(destinationSolution => this.destination = destinationSolution);
                this.saveHunt()
            },

            getCurrentTime() {

            },


            saveHunt() {

            },


            /**
             * Cancels the creation or editing of a treasure hunt by emitting a value to the treasureHuntList
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * If the treasure hunt is being edited, retrieves the destination solution to the hunt
             */
            getTreasureHuntDestination(updateHuntDestination) {
                if (this.inputTreasureHunt.id != null) {
                    let self = this;
                    fetch(`/v1/treasureHuntDest/` + this.inputTreasureHunt.id, {
                        method: 'GET'
                    })
                        .then(this.checkStatus)
                        .then(this.parseJSON)
                        .then(updateHuntDestination)
                }
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
                throw error;
            },


            /**
             * Converts the retrieved Http response to a Json format.
             *
             * @param response the Http response.
             * @returns the Http response body as Json.
             */
            parseJSON(response) {
                console.log(response)
                return response.json();
            }

        }
    }
</script>

<style scoped>

</style>


