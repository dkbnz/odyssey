<template>
    <div>
        <b-list-group-item class="list-item">
            <b-row class="h-50 w-100 align-middle">
                <b-col cols="8">
                    <b-form-input v-model="treasureHunt.riddle" v-if="edit" placeholder="Enter a riddle"></b-form-input>
                    <h5 class="mb-1" v-else>{{treasureHunt.riddle}}</h5>
                </b-col>
                <b-col>
                    <h6 class="mb-1 text-right" v-if="treasureHunt.destination">{{treasureHunt.destination.name}}</h6>
                    <h6 class="mb-1 text-right" v-else>Select a destination</h6>
                </b-col>
            </b-row>
            <b-row class="h-50 w-100">
                <b-col cols="4">
                    <p class="w-50">
                        Start Date:
                    </p>
                    <b-form-input v-if="edit" v-model="treasureHunt.startDate" type="datetime-local" value="2011-08-19T13:45:00" class="w-50"></b-form-input>
                    <p v-else>
                        {{treasureHunt.startDate}}
                    </p>
                </b-col>
                <b-col cols="4" class="justify-content-between">
                    <p class="w-50">
                        End Date:
                    </p>
                    <b-form-input v-if="edit" v-model="treasureHunt.endDate" type="datetime-local" value="2011-08-19T13:45:00" class="w-50"></b-form-input>
                    <p v-else>
                        {{treasureHunt.endDate}}
                    </p>
                </b-col>

                <b-col>
                    <div class="text-right ">
                        <b-button @click="edit = false" variant="primary" v-if="edit">Save</b-button>
                        <b-button @click="edit = true" variant="primary" v-else>Edit</b-button>
                        <b-button @click="" variant="danger">Delete</b-button>
                    </div>
                </b-col>
            </b-row>
        </b-list-group-item>
    </div>


    <!--<div :class="containerClass">-->

        <!--<h1 class="page-title">{{ heading }}</h1>-->
        <!--<p class="page-title"><i>{{ subHeading }}</i></p>-->

        <!--<b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>-->

        <!--&lt;!&ndash; Displays success alert and progress bar on trip creation as a loading bar-->
        <!--for the trip being added to the database &ndash;&gt;-->
        <!--<b-alert-->
                <!--:show="dismissCountDown"-->
                <!--@dismiss-count-down="countDownChanged"-->
                <!--@dismissed="dismissCountDown=0"-->
                <!--dismissible-->
                <!--variant="success">-->
            <!--<p>Trip Successfully Saved</p>-->
            <!--<b-progress-->
                    <!--:max="dismissSecs"-->
                    <!--:value="dismissCountDown"-->
                    <!--height="4px"-->
                    <!--variant="success"-->
            <!--&gt;</b-progress>-->
        <!--</b-alert>-->

        <!--<b-row>-->
            <!--<b-col>-->
                <!--<b-card>-->
                    <!--<b-form>-->

                        <!--<b-container fluid>-->
                            <!--<b-form-group-->
                                    <!--id="treasure_hunt_riddle-field"-->
                                    <!--label="Treasure Hunt Riddle:"-->
                                    <!--label-for="treasure_hunt_riddle">-->
                                <!--<b-form-textarea :type="'expandable-text'"-->
                                              <!--id="treasure_hunt_riddle"-->
                                              <!--trim-->
                                              <!--v-model="inputTreasureHunt.riddle"></b-form-textarea>-->
                            <!--</b-form-group>-->
                        <!--</b-container>-->


                        <!--<b-form @reset="resetDestForm">-->
                            <!--<b-container fluid>-->
                                <!--<b-row>-->
                                    <!--<b-col>-->
                                        <!--<h6 class="mb-1">Selected Destination:</h6>-->
                                        <!--<b-list-group>-->
                                            <!--<b-list-group-item href="#" class="flex-column align-items-start"-->
                                                               <!--id="selectedDestination"-->
                                                               <!--:disabled="selectedDestination.length === '{}'">-->
                                                <!--<div class="d-flex w-100 justify-content-between">-->
                                                    <!--<h5 class="mb-1" v-if="selectedDestination.name">-->
                                                        <!--{{selectedDestination.name}}-->
                                                    <!--</h5>-->
                                                    <!--<h5 class="mb-1" v-else>Select a Destination</h5>-->

                                                <!--</div>-->

                                                <!--<p>-->
                                                    <!--{{selectedDestination.district}}-->
                                                <!--</p>-->
                                                <!--<p>-->
                                                    <!--{{selectedDestination.country}}-->
                                                <!--</p>-->
                                            <!--</b-list-group-item>-->
                                        <!--</b-list-group>-->
                                    <!--</b-col>-->

                                    <!--<b-col>-->
                                        <!--<b-form-group-->
                                                <!--id="startDate-field"-->
                                                <!--label="Start Date:"-->
                                                <!--label-for="startDate">-->
                                            <!--<b-row>-->
                                            <!--<b-col cols="6">-->
                                                <!--<b-form-input :type="'date'"-->
                                                                          <!--id="startDate"-->
                                                                          <!--min='getCurrentDate()'-->
                                                                          <!--max='9999-12-31'-->
                                                                          <!--trim-->
                                                                          <!--v-model="startDate"-->
                                                                          <!--:state="validateStartDate">-->

                                                <!--</b-form-input>-->
                                            <!--</b-col>-->

                                            <!--<b-col cols="6">-->
                                                <!--<b-form-input :type="'time'"-->
                                                              <!--id="startTime"-->
                                                              <!--min='getCurrentTime()'-->
                                                              <!--max=''-->
                                                              <!--trim-->
                                                              <!--v-model="startTime"-->
                                                              <!--:state="validateStartDate">-->
                                                <!--</b-form-input>-->
                                            <!--</b-col>-->
                                            <!--</b-row>-->

                                        <!--</b-form-group>-->
                                        <!--<b-form-group-->
                                                <!--id="endDate-field"-->
                                                <!--label="Expiration Date:"-->
                                                <!--label-for="endDate">-->
                                            <!--<b-col cols="6"></b-col>-->
                                            <!--<b-col cols="6"></b-col>-->

                                            <!--<b-form-input :type="'datetime-local'"-->
                                                          <!--id="endDate"-->
                                                          <!--min='getCurrentTime()'-->
                                                          <!--max='9999-12-31T00:00'-->
                                                          <!--trim-->
                                                          <!--v-model="endDate"-->
                                                          <!--:state="validateEndDate"></b-form-input>-->
                                        <!--</b-form-group>-->
                                    <!--</b-col>-->


                                <!--</b-row>-->

                                <!--&lt;!&ndash;<b-button @click="checkDestination"&ndash;&gt;-->
                                <!--&lt;!&ndash;class="mr-2 float-right"&ndash;&gt;-->
                                <!--&lt;!&ndash;variant="primary">&ndash;&gt;-->
                                <!--&lt;!&ndash;Add Destination&ndash;&gt;-->
                                <!--&lt;!&ndash;</b-button>&ndash;&gt;-->

                            <!--</b-container>-->
                        <!--</b-form>-->
                    <!--</b-form>-->


                <!--</b-card>-->
                <!--<b-button @click="validateTreasureHunt" block variant="primary">Save-->

                <!--</b-button>-->
            <!--</b-col>-->
        <!--</b-row>-->

    <!--</div>-->
</template>

<script>
    import DestinationSidebar from "../destinations/destinationSidebar";
    import BCol from "bootstrap-vue/es/components/layout/col";
    export default {
        name: "treasureHuntListItem",
        props: {
            selectedDestination: Object,
            treasureHunt: {
                default: function () {
                    return {
                        id: null,
                        riddle: "",
                        destination: {
                            name: ""
                        },
                        startDate: "",
                        endDate: ""
                    }
                }
            }
        },

        mounted: function() {
            this.edit = (this.treasureHunt.id == null)
        },

        data() {
            return {
                edit: false
            }
        },

        watch: {
            selectedDestination: function() {
                if(this.edit) {
                    console.log(this.selectedDestination)
                    this.treasureHunt.destination = this.selectedDestination;
                    console.log(this.treasureHunt)
                }
            }

        },

        methods: {
        }
    }
</script>

<style scoped>

    .list-item{
        height: 180px;
    }

</style>


