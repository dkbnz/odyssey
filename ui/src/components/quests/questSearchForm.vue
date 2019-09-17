<template>
    <div>
        <h4 class="page-title">Search Quests</h4>
        <div>
            <!--Input fields for searching for quests-->
            <b-form-group
                    id="title-field"
                    label="Quest Title:"
                    label-for="title">
                <b-form-input id="title"
                              v-model="searchTitle"
                              :state="questTitleValidation"></b-form-input>
            </b-form-group>

            <p>Number of Objectives:</p>
            <b-card>
                <b-row>
                    <b-col>
                        <b-form-group
                                id="option-field"
                                label="Operator:"
                                label-for="option">
                            <!--Dropdown field for comparision fields-->
                            <b-form-select id="option" trim v-model="searchOperator">
                                <template slot="first">
                                    <option value="=">Equal to</option>
                                </template>
                                <option :value="option.value" v-for="option in operatorOptions"
                                        :state="operatorOptionsValidation">
                                    {{option.text}}
                                </option>
                            </b-form-select>
                        </b-form-group>
                    </b-col>

                    <b-col>
                        <b-form-group
                                id="number-objectives-field"
                                label="Amount:"
                                label-for="number-objectives">
                            <b-form-input id="number-objectives"
                                          trim
                                          v-model="searchNumberObjective"
                                          :state="numberObjectiveValidation">
                            </b-form-input>
                        </b-form-group>
                    </b-col>
                </b-row>
            </b-card>

            <p>Created By:</p>
            <b-card>
                <b-row>
                    <b-col>
                        <b-form-group
                                id="created-first-field"
                                label="First Name:"
                                label-for="created-first">
                            <b-form-input id="created-first"
                                          trim
                                          v-model="searchCreatedFirst"
                                          :state="createdFirstValidation">
                            </b-form-input>
                        </b-form-group>
                    </b-col>

                    <b-col>
                        <b-form-group
                                id="created-last-field"
                                label="Last Name:"
                                label-for="created-last">
                            <b-form-input id="created-last"
                                          trim
                                          v-model="searchCreatedLast"
                                          :state="createdLastValidation">
                            </b-form-input>
                        </b-form-group>
                    </b-col>
                </b-row>
            </b-card>

            <b-form-group
                    id="country-field"
                    label="Country:"
                    label-for="country">
                <!--Dropdown field for country types-->
                <b-form-select id="country" trim v-model="searchCountry">
                    <template slot="first">
                        <option value="">-- Any --</option>
                    </template>
                    <option :value="country.name" v-for="country in countryList"
                            :state="countryValidation">
                        {{country.name}}
                    </option>
                </b-form-select>
            </b-form-group>

            <b-button @click="searchQuests" block :disabled="!allFieldValidation" variant="primary">Search</b-button>
        </div>
    </div>
</template>

<script>

    export default {
        name: "questSearchForm.vue",

        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            },
        },

        data() {
            return {
                searchTitle: "",
                searchOperator: "=",
                searchNumberObjective: "",
                searchCreatedFirst: "",
                searchCreatedLast: "",
                searchCountry: "",
                operatorOptions: [
                    {value: '>', text: "Greater than"},
                    {value: '<', text: "Less than"},
                ],
                countryList: Array
            }
        },

        mounted() {
            this.getCountries()
        },

        computed: {
            /**
             * Validates the input fields based on regex.
             *
             * @return {*} true if input is valid.
             */
            questTitleValidation() {
                if (this.searchTitle.length === 0) {
                    return null;
                }
                return this.searchTitle.length > 0;
            },


            operatorOptionsValidation() {
                if (this.searchOperator === null) {
                    return null;
                }
                return this.searchOperator.length > 0 || this.searchOperator !== null;
            },


            numberObjectiveValidation() {
                if (this.searchNumberObjective.length === 0) {
                    return null;
                }
                return (!isNaN(this.searchNumberObjective) &&
                    !(this.searchNumberObjective < 1 || this.searchNumberObjective.includes(".")))
            },


            createdFirstValidation() {
                if (this.searchCreatedFirst.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.searchCreatedFirst);
            },


            createdLastValidation() {
                if (this.searchCreatedLast.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.searchCreatedLast);
            },


            countryValidation() {
                if (this.searchCountry === null) {
                    return null;
                }
                return this.searchCountry.length > 0 || this.searchCountry !== null;
            },


            allFieldValidation() {
                return (this.validateFields(this.questTitleValidation)
                && this.validateFields(this.operatorOptionsValidation)
                && this.validateFields(this.numberObjectiveValidation)
                && this.validateFields(this.createdFirstValidation)
                && this.validateFields(this.createdLastValidation)
                && this.validateFields(this.countryValidation))
            }
        },

        methods: {
            /**
             * Sets the countries list to the list of countries from the country api
             */
            getCountries() {
                return fetch("https://restcountries.eu/rest/v2/all", {
                    dataType: 'html'
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.countryList = responseBody;
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast([{message: "An unexpected error occurred"}]);
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            },


            /**
             * Sets values for search.
             */
            searchQuests() {
                if (this.allFieldValidation) {
                    this.queryQuests();
                }
            },


            /**
             * Checks each of the validation fields to ensure they are return either null (no value is given), or the
             * field is valid.
             *
             * @return {boolean} true if the fields are valid.
             */
            validateFields(validationField) {
                if (validationField === null || validationField === true) {
                    return true;
                }
            },


            /**
             * Runs a query which searches through the quests in the database and returns all which
             * follow the search criteria.
             *
             * @return {Promise<Response | never>}
             */
            queryQuests() {
                let self = this;
                let searchQuery =
                    "?title=" + this.searchTitle +
                    "&operator=" + this.searchOperator +
                    "&objective=" + this.searchNumberObjective +
                    "&first_name=" + this.searchCreatedFirst +
                    "&last_name=" + this.searchCreatedLast +
                    "&country=" + this.searchCountry;

                return fetch(`/v1/quests` + searchQuery, {})
                .then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                        self.$emit('searched-quests', responseBody);
                }).catch(function (response) {
                    if (response.status > 404) {
                        self.showErrorToast([{message: "An unexpected error occurred"}]);
                    } else {
                        response.json().then(function(responseBody) {
                            self.showErrorToast(responseBody);
                        });
                    }
                });
            }
        },
    }
</script>