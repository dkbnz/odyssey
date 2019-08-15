<template>
    <div>
        <b-list-group class="scroll">
            <!--Successful treasure hunt delete alert -->
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
                               v-if="creatingHunt" draggable="false">
                <add-treasure-hunt :profile="profile" :heading="'Create'"
                                   @cancelCreate="cancelCreate"
                                   :selectedDestination="selectedDestination"
                                   @destination-select="$emit('destination-select')"
                                   @successCreate="message => showSuccess(message)">

                </add-treasure-hunt>
            </b-list-group-item>
            <div v-if="!sideBarView">
                <b-list-group-item href="#" class="flex-column justify-content-center"
                                   v-if="!creatingHunt" draggable="false">
                    <div class="d-flex justify-content-center">
                        <b-button variant="success"
                                  @click="addTreasureHunt" block>
                            Add a New Treasure Hunt
                        </b-button>
                    </div>
                </b-list-group-item>
            </div>

            <b-list-group-item v-for="treasureHunt in (foundTreasureHunts)" href="#"
                               class="flex-column align-items-start"
                               :key="treasureHunt.id"
                               draggable="false"
                               @click="emitTreasureHunt(treasureHunt)">
                <template v-if="!editingHunt && !(activeId === treasureHunt.id)">
                        <h4>Riddle</h4>
                        {{treasureHunt.riddle}}
                        <h4 class="buttonMarginsTop">Radius</h4>
                        {{getRadiusValue(treasureHunt.radius)}}
                    <div v-if="yourTreasureHunts" class="buttonMarginsTop">
                        <h4>Answer</h4>
                        <p>{{treasureHunt.destination.name}}</p>
                    </div>

                    <b-row v-if="yourTreasureHunts">
                        <b-col>
                            <b-button v-if="!sideBarView" variant="warning" @click="setActiveId(treasureHunt)" block>
                                Edit
                            </b-button>
                        </b-col>
                        <b-col>
                            <b-button variant="danger" v-if="!sideBarView" @click="setTreasureHunt(treasureHunt)" block>
                                Delete
                            </b-button>
                        </b-col>
                    </b-row>
                </template>
                <add-treasure-hunt v-else
                                   :profile="profile"
                                   :heading="'Edit'"
                                   :input-treasure-hunt="copiedTreasureHunt"
                                   @cancelCreate="cancelEdit"
                                   @destination-select="$emit('destination-select')"
                                   :selectedDestination="selectedDestination">

                </add-treasure-hunt>
                <!--Treasure Hunt component-->
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults"
                               draggable="false">
                <div class="d-flex justify-content-center">
                    <b-spinner label="Loading..."></b-spinner>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center"
                               v-if="!loadingResults && foundTreasureHunts.length === 0" draggable="false">
                <div class="d-flex justify-content-center">
                    <strong>No Treasure Hunts</strong>
                </div>
            </b-list-group-item>

        </b-list-group>
        <!-- Confirmation modal for deleting a treasure hunt. -->
        <b-modal hide-footer id="deleteTreasureHuntModal" ref="deleteTreasureHuntModal" title="Delete Treasure Hunt">
            <div class="d-block">
                Are you sure that you want to delete this Treasure Hunt?
            </div>
            <!--Error when deleting treasure hunt alert-->
            <b-alert
                    v-model="deleteAlertError"
                    dismissible
                    variant="danger">
                <p>{{deleteAlertMessage}}</p>
            </b-alert>
            <b-button
                    class="mr-2 float-right"
                    variant="danger"
                    @click="deleteTreasureHunt">Delete
            </b-button>
            <b-button
                    @click="dismissModal('deleteTreasureHuntModal')"
                    class="mr-2 float-right">Cancel
            </b-button>
        </b-modal>
    </div>
</template>

<script>
    import AddTreasureHunt from "./treasureHuntItem";

    export default {
        name: "treasureHuntList",

        props: {
            profile: Object,
            adminView: {
                default: function () {
                    return false;
                }
            },
            yourTreasureHunts: Boolean,
            selectedDestination: {},
            refreshTreasureHunts: Boolean,
            sideBarView: {
                default: function () {
                    return false;
                }
            }
        },

        data() {
            return {
                foundTreasureHunts: [],
                loadingResults: true,
                moreResults: true,
                creatingHunt: false,
                editingHunt: false,
                activeId: 0,
                treasureHuntId: null,
                dismissSeconds: 3,
                dismissCountDown: 0,
                alertText: "",
                copiedTreasureHunt: null,
                deleteAlertError: false,
                deleteAlertMessage: ""
            }
        },

        mounted() {
            this.getMore();
        },

        watch: {
            refreshTreasureHunts() {
                this.getMore();
            }
        },

        methods: {
            /**
             * Used to convert the treasureHunt object into a Json object.
             */
            copyTreasureHunt(treasureHunt) {
                this.copiedTreasureHunt = JSON.parse(JSON.stringify(treasureHunt))
            },


            /**
             * Function to retrieve more treasure hunts when a user reaches the bottom of the list.
             */
            getMore() {
                this.foundTreasureHunts = [];
                if (this.yourTreasureHunts) {
                    this.queryYourTreasureHunts();
                } else {
                    this.queryTreasureHunts();
                }
            },


            /**
             * Send the Http request to delete the specified treasure hunt.
             */
            deleteTreasureHunt() {
                let self = this;
                fetch(`/v1/treasureHunts/` + this.treasureHuntId, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.ok) {
                        self.getMore();
                        self.$refs['deleteTreasureHuntModal'].hide();
                        self.alertText = "Treasure Hunt Successfully Deleted";
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
             * Runs a query which searches through the treasure hunts in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryTreasureHunts() {
                return fetch(`/v1/treasureHunts`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.foundTreasureHunts = data;
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the treasure hunts in the database and returns only
             * treasure hunts created by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourTreasureHunts() {
                if (this.profile.id !== undefined) {
                    return fetch(`/v1/treasureHunts/` + this.profile.id, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.foundTreasureHunts = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Changes creatingHunt to true to show the create treasure hunt window, and calls function to close edit
             * windows,             *
             */
            addTreasureHunt() {
                this.creatingHunt = true;
                this.cancelEdit()
            },


            /**
             * Changes the active treasure hunt ID to the inputted one, and sets creatingHunt to false to hide creation
             * box.
             * @param treasureHunt the treasure hunt to be changed to.
             */
            setActiveId(treasureHunt) {
                this.copyTreasureHunt(treasureHunt);
                this.activeId = treasureHunt.id;
                this.creatingHunt = false
            },


            /**
             * Changes the treasure hunt ID to the currently selected treasure hunt id.
             * Dismisses the delete treasure hunt modal.
             *
             */
            setTreasureHunt(treasureHunt) {
                this.treasureHuntId = treasureHunt.id;
                this.$refs['deleteTreasureHuntModal'].show();
            },


            /**
             * Sets editingHunt to false and the active hunt ID to 0 to close any open hunt editing box. Emits signal
             * to hide destination search box. clears selected destination.
             */
            cancelEdit() {
                this.getMore();
                this.editingHunt = false;
                this.activeId = 0;
                this.$emit('hide-destinations');
            },


            /**
             * Sets creatingHunt to false and emits signal to hide destination search box. clears selected destination.
             */
            cancelCreate() {
                this.getMore();
                this.creatingHunt = false;
                this.$emit('hide-destinations');
            },


            /**
             * Sets the message for the success alert to the inputted message and runs showAlert to show the success
             * message.
             * @param message to be set as the alert message.
             */
            showSuccess(message) {
                this.getMore();
                this.queryYourTreasureHunts();
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
             * Used to dismiss the delete a treasure hunt confirmation modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
                this.deleteAlertError = false;
            },


            /**
             * Displays the countdown alert on the successful deletion of a treasure hunt.
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
             * Emits the selected treasure hunt when selecting on the side bar for quests.
             */
            emitTreasureHunt(treasureHunt) {
                if (this.sideBarView) {
                    this.$emit('select-treasure-hunt', treasureHunt)
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
            AddTreasureHunt
        }
    }
</script>