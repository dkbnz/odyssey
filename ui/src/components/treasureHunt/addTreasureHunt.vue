<template>

    <div :class="containerClass">

        <h1 class="page-title">{{ heading }}</h1>
        <p class="page-title"><i>{{ subHeading }}</i></p>

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
                <b-card>
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
                                                               id="selectedDestination"
                                                               :disabled="selectedDestination.length === '{}'">
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
                                                                          v-model="startDate"
                                                                          :state="validateStartDate">

                                                </b-form-input>
                                            </b-col>

                                            <b-col cols="6">
                                                <b-form-input :type="'time'"
                                                              id="startTime"
                                                              min='getCurrentTime()'
                                                              max=''
                                                              trim
                                                              v-model="startTime"
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

                                            <b-form-input :type="'datetime-local'"
                                                          id="endDate"
                                                          min='getCurrentTime()'
                                                          max='9999-12-31T00:00'
                                                          trim
                                                          v-model="endDate"
                                                          :state="validateEndDate"></b-form-input>
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


                </b-card>
                <b-button @click="validateTreasureHunt" block variant="primary">Save

                </b-button>
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
                        destination: Object,
                        startDate: "",
                        endDate: ""
                    }
                }
            },
            heading: String,
            subHeading: String,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },

        data() {
            return {
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


        methods: {

            resetDestForm() {

            },


            validateTreasureHunt() {
                this.saveHunt()
            },

            getCurrentTime() {

            },

            validateStartDate() {

            },

            validateEndDate() {

            },


            saveHunt() {

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

<style scoped>

</style>


