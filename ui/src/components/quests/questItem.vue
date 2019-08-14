<template>

    <div>


        <h1 class="page-title">{{ heading }} a Quest!</h1>

        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!-- Displays success alert and progress bar on quest creation as a loading bar
        for the quest being added to the database -->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Quest Successfully Saved</p>
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
                                id="quest_title-field"
                                label="Quest Title:"
                                label-for="quest_title">
                            <b-form-textarea :type="'expandable-text'"
                                             id="quest_title"
                                             trim
                                             v-model="inputQuest.title"
                                             :state="validateTitle"></b-form-textarea>
                        </b-form-group>
                    </b-container>


                    <b-form>
                        <b-container fluid>
                            <b-form-group
                                    id="startDate-field">
                                <b-row>
                                    <b-col cols="6">
                                        <label>Start Date:</label>
                                        <b-form-input :type="'date'"
                                                      id="startDate"
                                                      min='getCurrentDate()'
                                                      max='9999-12-31'
                                                      trim
                                                      v-model="inputQuest.startDate"
                                                      :state="validateStartDate">

                                        </b-form-input>
                                    </b-col>
                                    <b-col cols="6">
                                        <label>Start Time:</label>
                                        <b-form-input :type="'time'"
                                                      id="startTime"
                                                      min='getCurrentTime()'
                                                      max=''
                                                      trim
                                                      v-model="startTime"
                                                      :state="validateStartTime">
                                        </b-form-input>
                                    </b-col>
                                </b-row>

                            </b-form-group>
                            <b-form-group
                                    id="endDate-field">
                                <b-col cols="6"></b-col>
                                <b-col cols="6"></b-col>

                                <b-row>
                                    <b-col cols="6">
                                        <label>Expiration Date:</label>
                                        <b-form-input :type="'date'"
                                                      id="endDate"
                                                      min='getCurrentDate()'
                                                      max='9999-12-31'
                                                      trim
                                                      v-model="inputQuest.endDate"
                                                      :state="validateEndDate">

                                        </b-form-input>
                                    </b-col>

                                    <b-col cols="6">
                                        <label>Expiration Time:</label>
                                        <b-form-input :type="'time'"
                                                      id="endTime"
                                                      min='getCurrentTime()'
                                                      max=''
                                                      trim
                                                      v-model="endTime"
                                                      :state="validateEndTime">
                                        </b-form-input>
                                    </b-col>
                                </b-row>
                            </b-form-group>
                        </b-container>
                    </b-form>
                </b-form>

                <div class="d-flex justify-content-center" >
                    <b-button variant="success"  @click="addNewTreasureHunt = !addNewTreasureHunt" block>Add a New Treasure Hunt</b-button>
                </div>

                <b-row v-if="addNewTreasureHunt">
                    <b-col cols="12">
                        <add-treasure-hunt
                                :profile="profile"
                                :heading="'Create'"
                                @destination-select="$emit('destination-select')"
                                :selectedDestination="selectedDestination">
                        </add-treasure-hunt>
                    </b-col>
                </b-row>

                <b-row>
                    <b-col cols="8">
                        <b-button @click="validateQuest" block variant="primary">
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
    import BCol from "bootstrap-vue/es/components/layout/col";
    import AddTreasureHunt from "../treasureHunt/treasureHuntItem";

    export default {
        name: "questItem",

        components: {
            BCol,
            AddTreasureHunt
        },

        props: {
            profile: Object,
            inputQuest: {
                default: function () {
                    return {
                        id: null,
                        title: "",
                        startDate: "",
                        endDate: "",
                    }
                }
            },
            newDestination: Object,
            selectedDestination: {
                default: function () {
                    return {}
                }
            },
            heading: String,
            containerClass: {
                default: function () {
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
                startTime: "",
                endTime: "23:59",
                displayedDestination: null,
                addNewTreasureHunt: false
            }
        },

        watch: {
            selectedDestination() {
                this.inputQuest.destination = this.selectedDestination;
                this.displayedDestination = this.selectedDestination;
            }
        },

        mounted() {
            this.splitDates();
            this.setDateTimeString();
        },

        computed: {
            /**
             * For new quest, checks the start date is after the current date.
             * For all other quests, checks the start date is either the same as or before the end date.
             *
             * @returns {boolean} true if start date is valid.
             */
            validateStartDate() {
                // For a new hunt, the start date must be after today.
                if ((this.inputQuest.startDate < this.getDateString() && !this.inputQuest.id)) {
                    return false;
                }
                // Otherwise, checks the start date is equal to or before the end date.
                return this.inputQuest.startDate <= this.inputQuest.endDate;
            },


            /**
             * Checks that the start time is not after or the same as the end time if the dates are the same,
             * and that the start time is not before the current time if the current date is today.
             *
             * @returns {boolean} true if start time is valid.
             */
            validateStartTime() {
                // For new quests, check the start time is after the current time.
                if (this.startTime === "" || this.startTime === undefined) {
                    return false
                }
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.startTime >= this.endTime) {
                        return false;
                    }
                }
                // If the dates are the same, check the start time is before the end time.
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.startTime >= this.endTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * Checks that the end time is not before or the same as the start time if the dates are the same.
             *
             * @returns {boolean} true if end time is valid.
             */
            validateEndTime() {
                if (this.inputQuest.startDate === this.inputQuest.endDate) {
                    if (this.endTime <= this.startTime) {
                        return false;
                    }
                }
                return true;
            },


            /**
             * For new quests, checks the end date is after the current date.
             * For all other quests, checks the end date is either the same as or after the start date.
             *
             * @returns {boolean} true if end date is valid.
             */
            validateEndDate() {
                // For a new quests, the end date must be after today.
                if (this.inputQuest.endDate < this.getDateString() && !this.inputQuest.id) {
                    return false;
                }
                // Otherwise, checks the end date is equal to or after the start date.
                return this.inputQuest.endDate >= this.inputQuest.startDate;
            },


            /**
             * Returns true if the inputted title has length greater than 0.
             *
             * @returns {Boolean} true if validated.
             */
            validateTitle() {
                if (this.inputQuest.title.length > 0) {
                    return true;
                }
                return null;
            },
        },

        methods: {
            /**
             * Gets the current date+time as a Date object.
             *
             * @returns Current Datetime.
             */
            getCurrentDate() {
                return new Date();
            },


            /**
             * sets the input values to be their proper string versions of current date/time.
             */
            setDateTimeString() {
                if (this.inputQuest.id === null) {
                    this.inputQuest.startDate = this.getDateString();
                    this.inputQuest.endDate = this.getDateString();
                    this.startTime = this.getTimeString();
                }
            },


            /**
             * Gets the current date as a string in YYYY-MM-DD format, including padding O's on month/day.
             *
             * @returns Current Date in YYYY-MM-DD String Format.
             */
            getDateString() {
                let today = this.getCurrentDate();
                return today.getFullYear() + '-' +
                    ((today.getMonth() + 1) < 10 ? "0" : "")
                    + (today.getMonth() + 1) + '-' +
                    (today.getDate() < 10 ? "0" : "") +
                    today.getDate();
            },


            /**
             * Gets the current time as a string in HH:MM format, including padding O's.
             *
             * @returns Current Time in HH:MM String Format.
             */
            getTimeString() {
                let today = this.getCurrentDate();
                return ((today.getHours()) < 10 ? "0" : "") +
                    today.getHours() + ":"
                    + ((today.getMinutes()) < 10 ? "0" : "") +
                    today.getMinutes();
            },


            /**
             * If all field validations pass on the active quest, saves the quest using either
             * updateQuest if there is an active editing ID or saveQuest otherwise (adding a new one).
             */
            validateQuest() {
                if (this.validateStartDate && this.validateStartTime && this.validateEndDate && this.validateEndTime
                    && this.validateTitle) {
                    if (this.inputQuest.id !== null) {
                        this.updateQuest();
                    } else {
                        this.saveQuest();
                    }
                } else {
                    this.errorMessage = "Not all fields have valid information!";
                    this.showError = true;
                }

            },


            /**
             * Creates formatted JSON of the currently active quest.
             *
             * @returns JSON string with fields 'title', 'treasureHunts', 'startDate', 'endDate'.
             */
            assembleQuest() {
                this.joinDates();
            },


            /**
             * POST's the currently active quest to the quests endpoint in JSON format, for newly creating
             * quests.
             */
            saveQuest() {
                this.assembleQuest();
                let self = this;
                fetch('/v1/quests/' + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputQuest)
                }).then(function (response) {
                    if (response.status >= 400) {
                        // Ensures the start and end date fields are not wiped after an error occurs.
                        this.splitDates();
                        // Converts response to text, this is then displayed on the frontend.
                        response.text().then(data => {
                            self.errorMessage = data;
                            self.showError = true;
                        });
                    } else {
                        self.$emit('successCreate', "Quest Successfully Created");
                        self.$emit('cancelCreate')
                    }
                });
            },


            /**
             * PUT's the currently active quest to the quests endpoint in JSON format, for edited
             * quests.
             */
            updateQuest() {
                this.assembleQuest();
                let self = this;
                fetch('/v1/quests/' + this.inputQuest.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.inputQuest)
                }).then(function (response) {
                    if (response.status >= 400) {
                        // Ensures the start and end date fields are not wiped after an error occurs.
                        this.splitDates();
                        // Converts response to text, this is then displayed on the frontend.
                        response.text().then(data => {
                            self.errorMessage = data;
                            self.showError = true;
                        });
                    } else {
                        self.$emit('cancelCreate')
                    }
                });
            },


            /**
             * Cancels the creation or editing of a quest by emitting a value to the questList.
             */
            cancelCreate() {
                this.$emit('cancelCreate');
            },


            /**
             * Splits the dates of the quest to put in the edit fields.
             */
            splitDates() {
                if (this.inputQuest.id !== null) {
                    this.inputQuest.startDate = new Date(this.inputQuest.startDate).toLocaleString();
                    let startDate = this.quest.startDate;
                    // The date is the values before the comma
                    this.inputQuest.startDate = this.inputQuest.startDate.split(", ")[0];
                    // Change format of dates from the backslash symbol, reverse the order, and join with hyphens.
                    this.inputQuest.startDate = this.inputQuest.startDate.split("/").reverse().join("-");
                    this.startTime = startDate.split(" ")[1];
                    // Splits by either the + or the - symbol. Removing the timezone.
                    this.startTime = this.startTime.split("+")[0];
                    this.startTime = this.startTime.split("-")[0];

                    this.inputQuest.endDate = new Date(this.inputQuest.endDate).toLocaleString();
                    let endDate = this.quest.endDate;
                    // The date is the values before the comma
                    this.inputQuest.endDate = this.inputQuest.endDate.split(", ")[0];
                    // Change format of dates from the backslash symbol, reverse the order, and join with hyphens.
                    this.inputQuest.endDate = this.inputQuest.endDate.split("/").reverse().join("-");
                    this.endTime = endDate.split(" ")[1];
                    // Splits by either the + or the - symbol. Removing the timezone.
                    this.endTime = this.endTime.split("+")[0];
                    this.endTime = this.endTime.split("-")[0];
                }
            },


            /**
             * Combines dates and times together from input fields and adds :00 on the end for seconds.
             */
            joinDates() {
                let timeOffset = this.formatOffset();

                if(this.startTime.length === 5) {
                    this.startTime += ":00";
                }

                if(this.endTime.length === 5) {
                    this.endTime += ":00";
                }

                this.inputQuest.startDate = this.inputQuest.startDate + " "
                    + this.startTime + timeOffset;

                this.inputQuest.endDate = this.inputQuest.endDate + " "
                    + this.endTime + timeOffset;

                delete this.inputQuest.startTime;
                delete this.inputQuest.endTime;
            },


            /**
             * Gets the local time offset and pads it to be 4 numbers long.
             */
            formatOffset() {
                let timeOffset = (Math.abs(new Date().getTimezoneOffset() / 60)).toString();

                let fullNumber = timeOffset.padStart(2, '0');
                fullNumber = fullNumber.padEnd(4, '0');

                let sign = (new Date().getTimezoneOffset() >= 0) ? "-" : "+";

                return sign + fullNumber;
            },


            /**
             * Displays the countdown alert on the successful saving of a quest.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Used to allow an alert to countdown on the successful saving of a quest.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            }
        }
    }
</script>

