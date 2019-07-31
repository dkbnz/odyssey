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
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="creatingHunt">
                <add-treasure-hunt :profile="profile" :heading="'Create'"
                                   @cancelCreate="cancelCreate"
                                   :selectedDestination="selectedDestination"
                                   @destination-select="$emit('destination-select')"
                                   @successCreate="message => showSuccess(message)">

                </add-treasure-hunt>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="!creatingHunt">
                <div class="d-flex justify-content-center">
                    <b-button variant="success"  @click="addTreasureHunt" block>Add a New Treasure Hunt</b-button>
                </div>
            </b-list-group-item>
            <b-list-group-item v-for="treasureHunt in (foundTreasureHunts)" href="#"
                               class="flex-column align-items-start"
                               :key="treasureHunt.id">
                <template v-if="!editingHunt && !(activeId === treasureHunt.id)">
                        <h4>Riddle</h4>
                        {{treasureHunt.riddle}}
                    <b-row class="buttonMarginsTop">
                        <b-col>
                            <h4>Start Date</h4>
                            {{new Date(treasureHunt.startDate)}}
                        </b-col>
                        <b-col>
                            <h4>End Date</h4>
                            {{new Date(treasureHunt.endDate)}}
                        </b-col>
                    </b-row>
                    <div v-if="yourTreasureHunts" class="buttonMarginsTop">
                        <h4>Answer</h4>
                        <p>{{treasureHunt.destination.name}}</p>
                    </div>

                    <b-row v-if="yourTreasureHunts">
                        <b-col>
                            <b-button variant="warning" @click="setActiveId(treasureHunt)" block>Edit</b-button>
                        </b-col>
                        <b-col>
                            <b-button variant="danger" @click="setTreasureHunt(treasureHunt)" block>Delete
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
            <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults">
                <div class="d-flex justify-content-center">
                    <b-spinner label="Loading..."></b-spinner>
                </div>
            </b-list-group-item>
            <b-list-group-item href="#" class="flex-column justify-content-center"
                               v-if="!loadingResults && foundTreasureHunts.length === 0">
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
                    ref="deleteAlertError"
                    dismissible
                    variant="danger">
                <p>Can not delete this Treasure Hunt</p>
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
            selectedDestination: {}
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
                copiedTreasureHunt: null
            }
        },

        mounted() {
            this.getMore();
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
                    this.queryYourTreasureHunts(this.profile);
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
                        self.$refs['deleteTreasureHuntModal'].hide();
                        self.getMore();
                        self.alertText = "Treasure Hunt Successfully Deleted";
                        self.showAlert();
                    }
                    else {
                        self.showError();
                    }
                });
            },


            /**
             * Runs a query which searches through the treasure hunts in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryTreasureHunts() {

                return fetch(`/v1/treasureHunts`, {})
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        for (let i = 0; i < data.length; i++) {
                            this.foundTreasureHunts.push(data[i]);
                        }
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the treasure hunts in the database and returns only
             * treasure hunts created by the profile.
             *
             * @param profile       the profile that owns the treasure hunts.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourTreasureHunts(profile) {

                return fetch(`/v1/treasureHunts/` + profile.id, {})
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        for (let i = 0; i < data.length; i++) {
                            this.foundTreasureHunts.push(data[i]);
                        }
                        this.loadingResults = false;
                    })
            },


            /**
             * Changes creatingHunt to true to show the create treasure hunt window, and calls function to close edit windows
             *
             */
            addTreasureHunt() {
                this.creatingHunt = true;
                this.cancelEdit()
            },


            /**
             * Changes the active treasure hunt ID to the inputted one, and sets creatingHunt to false to hide creation box
             * @param id the id of the treasure hunt to be changed to
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
             * Sets editingHunt to false and the active hunt ID to 0 to close any open hunt editing box
             */
            cancelEdit() {
                this.editingHunt = false;
                this.activeId = 0;
                this.$emit('hide-destinations')
                this.selectedDestination = {};
            },

            cancelCreate() {
                this.creatingHunt = false;
                this.$emit('hide-destinations')
                this.selectedDestination = {};
            },



            showSuccess(message) {
                this.alertText = message;
                this.showAlert();
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
                this.$refs['deleteAlertError'].hide();
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
             * Displays an error alert on the delete modal if deleting a destination is not successful.
             */
            showError() {
                this.$refs['deleteAlertError'].show();
            }
        },

        components: {
            AddTreasureHunt
        }
    }
</script>

<style scoped>

</style>