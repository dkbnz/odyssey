<template>
    <div>
        <b-list-group class="scroll">
            <!--Successful objective delete alert -->
            <b-alert
                    :show="dismissCountDown"
                    @dismiss-count-down="countDownChanged"
                    @dismissed="dismissCountDown=0"
                    dismissible
                    variant="success">
                <p>{{alertText}}</p>
                <b-progress
                        :max="dismissSeconds"
                        :value="dismissCountDown"
                        height="4px"
                        variant="success"
                ></b-progress>
            </b-alert>
            <b-list-group-item href="#" class="flex-column justify-content-center"
                               v-if="creatingObjective" draggable="false">
                <add-objective :profile="profile" :heading="'Create'"
                                   @cancelCreate="cancelCreate"
                                   :selectedDestination="selectedDestination"
                                   @destination-select="$emit('destination-select')"
                                   @successCreate="message => showSuccess(message)">

                </add-objective>
            </b-list-group-item>
            <div v-if="!sideBarView">
                <b-list-group-item href="#" class="flex-column justify-content-center"
                                   v-if="!creatingObjective" draggable="false">
                    <div class="d-flex justify-content-center">
                        <b-button variant="success"
                                  @click="addObjective" block>
                            Add a New Objective
                        </b-button>
                    </div>
                </b-list-group-item>
            </div>

            <b-list-group-item v-for="objective in (foundObjectives)" href="#"
                               class="flex-column align-items-start"
                               :key="objective.id"
                               draggable="false"
                               @click="emitObjective(objective)">
                <template v-if="!editingObjective && !(activeId === objective.id)">
                        <h4>Riddle</h4>
                        {{objective.riddle}}

                    <div v-if="yourObjectives" class="buttonMarginsTop">
                        <h4>Radius</h4>
                        {{getRadiusValue(objective.radius)}}
                        <h4 class="buttonMarginsTop">Answer</h4>
                        <p>{{objective.destination.name}}</p>
                    </div>

                    <b-row v-if="yourObjectives">
                        <b-col>
                            <b-button v-if="!sideBarView" variant="warning" @click="setActiveId(objective)" block>
                                Edit
                            </b-button>
                        </b-col>
                        <b-col>
                            <b-button variant="danger" v-if="!sideBarView" @click="setObjective(objective)" block>
                                Delete
                            </b-button>
                        </b-col>
                    </b-row>
                </template>
                <add-objective v-else
                                   :profile="profile"
                                   :heading="'Edit'"
                                   :input-objective="copiedObjective"
                                   @cancelCreate="cancelEdit"
                                   @destination-select="$emit('destination-select')"
                                   :selectedDestination="selectedDestination">

                </add-objective>
                <!--Objective component-->
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults"
                               draggable="false">
                <div class="d-flex justify-content-center">
                    <b-spinner label="Loading..."></b-spinner>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center"
                               v-if="!loadingResults && foundObjectives.length === 0" draggable="false">
                <div class="d-flex justify-content-center">
                    <strong>No Objectives</strong>
                </div>
            </b-list-group-item>

        </b-list-group>
        <!-- Confirmation modal for deleting a objective. -->
        <b-modal hide-footer id="deleteObjectiveModal" ref="deleteObjectiveModal" title="Delete Objective">
            <div class="d-block">
                Are you sure that you want to delete this Objective?
            </div>
            <!--Error when deleting objective alert-->
            <b-alert
                    v-model="deleteAlertError"
                    dismissible
                    variant="danger">
                <p>{{deleteAlertMessage}}</p>
            </b-alert>
            <b-button
                    class="mr-2 float-right"
                    variant="danger"
                    @click="deleteObjective">Delete
            </b-button>
            <b-button
                    @click="dismissModal('deleteObjectiveModal')"
                    class="mr-2 float-right">Cancel
            </b-button>
        </b-modal>
    </div>
</template>

<script>
    import AddObjective from "./objectiveItem";

    export default {
        name: "objectiveList",

        props: {
            profile: Object,
            adminView: {
                default: function () {
                    return false;
                }
            },
            yourObjectives: Boolean,
            selectedDestination: {},
            refreshObjectives: Boolean,
            sideBarView: {
                default: function () {
                    return false;
                }
            }
        },

        data() {
            return {
                foundObjectives: [],
                loadingResults: true,
                moreResults: true,
                creatingObjective: false,
                editingObjective: false,
                activeId: 0,
                objectiveId: null,
                dismissSeconds: 3,
                dismissCountDown: 0,
                alertText: "",
                copiedObjective: null,
                deleteAlertError: false,
                deleteAlertMessage: ""
            }
        },

        mounted() {
            this.getMore();
        },

        watch: {
            refreshObjectives() {
                this.getMore();
            }
        },

        methods: {
            /**
             * Used to convert the objective object into a Json object.
             */
            copyObjective(objective) {
                this.copiedObjective = JSON.parse(JSON.stringify(objective))
            },


            /**
             * Function to retrieve more objectives when a user reaches the bottom of the list.
             */
            getMore() {
                this.foundObjectives = [];
                if (this.yourObjectives) {
                    this.queryYourObjectives();
                } else {
                    this.queryObjectives();
                }
            },


            /**
             * Send the Http request to delete the specified objective.
             */
            deleteObjective() {
                let self = this;
                fetch(`/v1/objectives/` + this.objectiveId, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.ok) {
                        self.getMore();
                        self.$refs['deleteObjectiveModal'].hide();
                        self.alertText = "Objective Successfully Deleted";
                        self.showAlert();
                    }
                    else {
                        // Converts response to text, this is then displayed on the frontend.
                        response.text().then(data => {
                            self.deleteAlertMessage = data;
                            self.deleteAlertError = true;
                        });
                    }
                });
            },


            /**
             * Runs a query which searches through the objectives in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryObjectives() {
                return fetch(`/v1/objectives`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.foundObjectives = data;
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the objectives in the database and returns only
             * objectives created by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourObjectives() {
                if (this.profile.id !== undefined) {
                    return fetch(`/v1/objectives/` + this.profile.id, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.foundObjectives = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Changes creatingObjective to true to show the create objective window, and calls function to close edit
             * windows,             *
             */
            addObjective() {
                this.creatingObjective = true;
                this.cancelEdit()
            },


            /**
             * Changes the active objective ID to the inputted one, and sets creatingObjective to false to hide creation
             * box.
             * @param objective the objective to be changed to.
             */
            setActiveId(objective) {
                this.copyObjective(objective);
                this.activeId = objective.id;
                this.creatingObjective = false
            },


            /**
             * Changes the objective ID to the currently selected objective id.
             * Dismisses the delete objective modal.
             *
             */
            setObjective(objective) {
                this.objectiveId = objective.id;
                this.$refs['deleteObjectiveModal'].show();
            },


            /**
             * Sets editingObjective to false and the active objective ID to 0 to close any open objective editing box. Emits signal
             * to hide destination search box. clears selected destination.
             */
            cancelEdit() {
                this.getMore();
                this.editingObjective = false;
                this.activeId = 0;
                this.$emit('hide-destinations');
            },


            /**
             * Sets creatingObjective to false and emits signal to hide destination search box. clears selected destination.
             */
            cancelCreate() {
                this.getMore();
                this.creatingObjective = false;
                this.$emit('hide-destinations');
            },


            /**
             * Sets the message for the success alert to the inputted message and runs showAlert to show the success
             * message.
             * @param message to be set as the alert message.
             */
            showSuccess(message) {
                this.getMore();
                this.queryYourObjectives();
                this.alertText = message;
                this.showAlert();
            },


            /**
             * Converts the Http response body to a Json.
             * @param response  the received Http response.
             * @returns {*}     the response body as a Json object.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Used to dismiss the delete a objective confirmation modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
                this.deleteAlertError = false;
            },


            /**
             * Displays the countdown alert on the successful deletion of a objective.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSeconds;
            },


            /**
             * Used to countdown the progress bar on an alert to countdown.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown;
            },


            /**
             * Emits the selected objective when selecting on the side bar for quests.
             */
            emitObjective(objective) {
                if (this.sideBarView) {
                    this.$emit('select-objective', objective)
                }
            },


            /**
             * Returns a string radius value determined by the size.
             *
             * @param radius    the radius to be changed.
             */
            getRadiusValue(radius) {
                if (radius < 1) {
                    return radius * 1000 + " Meters"
                }
                return radius + " Km";
            }
        },

        components: {
            AddObjective
        }
    }
</script>