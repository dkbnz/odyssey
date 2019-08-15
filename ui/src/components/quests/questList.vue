<template>
    <div>
        <b-row>
            <b-col cols="8">
                <b-list-group class="scroll">
                    <!--Successful quest alert -->
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
                                       v-if="creatingQuest"
                                       draggable="false">
                        <quest-item
                                :selected-treasure-hunt="selectedTreasureHunt"
                                :heading="'Create'"
                                :profile="profile"
                                @cancelCreate="cancelCreate"
                                :selectedDestination="selectedDestination"
                                @TH-side-bar="showHideBar => this.showDestinations = showHideBar"
                                @Your-TH-side-bar="showHideBar => this.showYourTreasureHunts = showHideBar"
                                @clear-treasure-hunt-values="clearTreasureHunt"
                        ></quest-item>
                    </b-list-group-item>
                    <b-list-group-item href="#" class="flex-column justify-content-center"
                                       v-if="!creatingQuest"
                                       draggable="false">
                        <div class="d-flex justify-content-center">
                            <b-button variant="success"  @click="addQuest" block>Add a New Quest</b-button>
                        </div>
                    </b-list-group-item>
                    <b-list-group-item v-for="quest in (foundQuests)" href="#"
                                       class="flex-column align-items-start"
                                       :key="quest.id"
                                       draggable="false">
                        <template v-if="!editingQuest && !(activeId === quest.id)">
                                <h4>Title</h4>
                                {{quest.title}}
                            <b-row class="buttonMarginsTop">
                                <b-col>
                                    <h4>Start Date</h4>
                                    {{new Date(quest.startDate)}}
                                </b-col>
                                <b-col>
                                    <h4>End Date</h4>
                                    {{new Date(quest.endDate)}}
                                </b-col>
                            </b-row>
                            <div v-if="yourQuests" class="buttonMarginsTop">
                                <h4>View Locations</h4>
                                <p>Show Treasure Hunts here</p>
                            </div>

                            <b-row v-if="yourQuests">
                                <b-col>
                                    <b-button variant="warning" @click="setActiveId(quest)" block>Edit</b-button>
                                </b-col>
                                <b-col>
                                    <b-button variant="danger" @click="setQuest(quest)" block>Delete
                                    </b-button>
                                </b-col>
                            </b-row>
                        </template>
                        <!--Quest component-->
                    </b-list-group-item>
                    <b-list-group-item href="#" class="flex-column justify-content-center" v-if="loadingResults">
                        <div class="d-flex justify-content-center">
                            <b-spinner label="Loading..."></b-spinner>
                        </div>
                    </b-list-group-item>
                    <b-list-group-item href="#" class="flex-column justify-content-center"
                                       v-if="!loadingResults && foundTreasureHunts.length === 0">
                        <div class="d-flex justify-content-center">
                            <strong>No Quests</strong>
                        </div>
                    </b-list-group-item>

                </b-list-group>
                <!-- Confirmation modal for deleting a quest. -->
                <b-modal hide-footer id="deleteQuestModal" ref="deleteQuestModal" title="Delete Quest">
                    <div class="d-block">
                        Are you sure that you want to delete this Quest?
                    </div>
                    <!--Error when deleting quest alert-->
                    <b-alert
                            v-model="deleteAlertError"
                            dismissible
                            variant="danger">
                        <p>{{deleteAlertMessage}}</p>
                    </b-alert>
                    <b-button
                            class="mr-2 float-right"
                            variant="danger"
                            @click="deleteQuest">Delete
                    </b-button>
                    <b-button
                            @click="dismissModal('deleteQuestModal')"
                            class="mr-2 float-right">Cancel
                    </b-button>
                </b-modal>
            </b-col>
            <b-col>
                <b-card>
                    <found-destinations
                            v-if="showDestinations"
                            :search-public="true"
                            :profile="profile"
                            @destination-click="destination => this.selectedDestination = destination">
                    </found-destinations>
                    <treasure-hunt-list
                            v-if="showYourTreasureHunts"
                            :yourTreasureHunts="true"
                            :profile="profile"
                            :sideBarView="true"
                            @select-treasure-hunt="setSelectedTreasureHunt">
                    </treasure-hunt-list>
                </b-card>
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import QuestItem from "./questItem";
    import FoundDestinations from "../destinations/destinationSearchList";
    import TreasureHuntList from "../treasureHunt/treasureHuntList";

    export default {
        name: "questList",

        props: {
            profile: Object,
            adminView: {
                default: function () {
                    return false;
                }
            },
            yourQuests: Boolean,
            selectedDestination: {},
            refreshQuests: Boolean
        },

        data() {
            return {
                foundQuests: [],
                loadingResults: true,
                moreResults: true,
                creatingQuest: false,
                editingQuest: false,
                activeId: 0,
                questId: null,
                dismissSeconds: 3,
                dismissCountDown: 0,
                alertText: "",
                copiedTreasureHunt: null,
                deleteAlertError: false,
                deleteAlertMessage: "",
                showDestinations: false,
                showYourTreasureHunts: false,
                selectedTreasureHuntTemplate: {
                    id: null,
                    destination: null,
                    riddle: "",
                    startDate: "",
                    startTime: "",
                    endDate: "",
                    endTime: "23:59",
                    radius: null
                },
                selectedTreasureHunt: {
                    id: null,
                    destination: null,
                    riddle: "",
                    startDate: "",
                    startTime: "",
                    endDate: "",
                    endTime: "23:59",
                    radius: null
                }

            }
        },

        mounted() {
            this.getMore();
        },

        watch: {
            refreshTreasureHunts() {
                this.getMore();
            },

            profile() {
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
             * Function to retrieve more quests when a user reaches the bottom of the list.
             */
            getMore() {
                this.foundQuests = [];
                if (this.yourQuests) {
                    this.queryYourQuests();
                } else {
                    this.queryQuests();
                }
            },


            /**
             * Send the Http request to delete the specified Quest.
             */
            deleteQuest() {
                let self = this;
                fetch(`/v1/quests/` + this.treasureHuntId, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.ok) {
                        self.getMore();
                        self.$refs['deleteQuestModal'].hide();
                        self.alertText = "Quest Successfully Deleted";
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
             * Runs a query which searches through the quests in the database and returns all.
             *
             * @returns {Promise<Response | never>}
             */
            queryQuests() {
                return fetch(`/v1/quests`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.foundQuests = data;
                        this.loadingResults = false;
                    });
            },


            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests created by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourQuests() {
                if (this.profile.id !== undefined) {
                    return fetch(`/v1/quests/` + this.profile.id, {})
                        .then(this.parseJSON)
                        .then((data) => {
                            this.foundQuests = data;
                            this.loadingResults = false;
                        })
                }

            },


            /**
             * Changes creatingQuest to true to show the create quest window, and calls function to close edit
             * windows,             *
             */
            addQuest() {
                this.creatingQuest = true;
                this.cancelEdit();
            },


            /**
             * Changes the active quest ID to the inputted one, and sets creatingQuest to false to hide creation
             * box.
             * @param quest     the quest to be changed to.
             */
            setActiveId(quest) {
                this.copyQuest(quest);
                this.activeId = quest.id;
                this.creatingQuest = false
            },


            /**
             * Changes the quest ID to the currently selected quest id.
             * Dismisses the delete quest modal.
             *
             */
            setQuest(quest) {
                this.questId = quest.id;
                this.$refs['deleteQuestModal'].show();
            },


            /**
             * Sets editingQuest to false and the active quest ID to 0 to close any open quest editing box.
             */
            cancelEdit() {
                this.getMore();
                this.editingQuest = false;
                this.activeId = 0;
                this.showDestinations = false;
            },


            /**
             * Sets creatingQuest to false and emits signal to hide destination search box. clears selected destination.
             */
            cancelCreate() {
                this.getMore();
                this.creatingQuest = false;
                this.showDestinations = false;
            },


            /**
             * Sets the message for the success alert to the inputted message and runs showAlert to show the success
             * message.
             * @param message to be set as the alert message.
             */
            showSuccess(message) {
                this.getMore();
                this.queryYourQuests();
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
             * Used to dismiss the delete a quest confirmation modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
                this.deleteAlertError = false;
            },


            /**
             * Displays the countdown alert on the successful deletion of a quest.
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
             * Sets the treasure hunt emitted from the select treasure hunt side bar.
             */
            setSelectedTreasureHunt(treasureHunt) {
                let newTreasureHunt = JSON.parse(JSON.stringify(treasureHunt));
                let radius = newTreasureHunt.radius;
                let radiusValue;
                let radiusList = [
                    {value: 0.005, text: "5 Meters"},
                    {value: 0.01, text: "10 Meters"},
                    {value: 0.02, text: "20 Meters"},
                    {value: 0.05, text: "50 Meters"},
                    {value: 0.1, text: "100 Meters"},
                    {value: 0.5, text: "500 Meters"},
                    {value: 1, text: "1 Km"},
                    {value: 5, text: "5 Km"},
                    {value: 10, text: "10 Km"},
                ];
                for (let i = 0; i < radiusList.length; i++) {
                    if (radius === radiusList[i].value) {
                        radiusValue = radiusList[i];
                    }
                }
                newTreasureHunt.radius = radiusValue;
                this.selectedTreasureHunt = newTreasureHunt;
            },


            /**
             * Clears the values for a treasure hunt.
             */
            clearTreasureHunt() {
                this.selectedTreasureHunt = JSON.parse(JSON.stringify(this.selectedTreasureHuntTemplate));
                this.selectedDestination = {};
            }
        },

        components: {
            TreasureHuntList,
            QuestItem,
            FoundDestinations
        }
    }
</script>