<template>
    <div v-if="profile.length !== 0">
        <nav-bar-main v-bind:profile="profile" v-if="!minimalInfo"></nav-bar-main>

        <div :class="containerClass">

            <h1 class="page-title" v-if="!minimalInfo">Find Profiles</h1>
            <p class="page-title" v-if="!minimalInfo"><i>Search for other travellers using any of the fields in the form
                below</i>
            </p>

            <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>


            <!-- Confirmation modal for deleting a profile. -->
            <b-modal ref="deleteProfileModal" id="deleteProfileModal" hide-footer title="Delete Profile">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedProfile.firstName}} {{selectedProfile.lastName}}"?
                </div>
                <b-button
                        @click="dismissModal()
                         deleteUser(selectedProfile);"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissModal()"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>

            <profile-search-form @search="searchProfiles"></profile-search-form>

            <!--Displays results from profile search in a table format-->
            <div style="margin-top: 40px">
                <profile-list
                        :profile-list="profiles"
                        :profile="profile"
                        :loading="retrievingProfiles"
                        :admin-view="minimalInfo"
                        @make-admin="makeAdmin"
                        @remove-admin="removeAdmin"
                        @admin-edit="profileEdited => $emit('admin-edit', profileEdited)"
                        @profile-delete="sendProfileToModal"
                >
                </profile-list>
            </div>
        </div>
        <footer-main></footer-main>
    </div>
    <div v-else>
        <unauthorised-prompt></unauthorised-prompt>
    </div>
</template>

<script>
    import ViewProfile from '../dash/viewProfile.vue'
    import Dash from '../dash/dashPage'
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import UnauthorisedPrompt from '../helperComponents/unauthorisedPromptPage'
    import ProfileList from "./profileList";
    import ProfileSearchForm from "./profileSearchForm";

    export default {
        name: "profilesPage",

        props: {
            profile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            minimalInfo: {
                default: function() {
                    return false;
                }
            },
            destinations: Array,
            perPage: {
                default: function () {
                    return 10;
                }
            },
            containerClass: {
                default: function () {
                    return 'containerMain';
                }
            },
            destinationTypes: Array
        },

        components: {
            ProfileSearchForm,
            ProfileList,
            ViewProfile,
            NavBarMain,
            FooterMain,
            Dash,
            UnauthorisedPrompt
        },

        data: function () {
            return {
                sortBy: 'firstName',
                sortDesc: false,
                showError: false,
                searchNationality: "",
                searchGender: "",
                searchMinAge: 0,
                searchMaxAge: 120,
                searchTravType: "",
                optionViews: [{value: 1, text: "1"}, {value: 5, text: "5"}, {value: 10, text: "10"}, {
                    value: 15,
                    text: "15"
                }],
                currentPage: 1,
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ],
                profiles: [],
                retrievingProfiles: false,
                selectedProfile: "",
                alertMessage: ""
            }
        },

        mounted() {
            this.queryProfiles();
        },

        computed: {
            /**
             * @returns the number of rows required in the table based on number of profiles to be displayed.
             */
            rows() {
                return this.profiles.length;
            },


            /**
             * Returns the fields that will be displayed on the table of profiles, depending on if the admin is looking
             * at the page or not.
             */
            fields() {
                if (!this.minimalInfo) {
                    return [
                        {key: 'profilePhoto', label: "Photo", sortable: true, class: 'tableWidthSmall'},
                        {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                        {key: 'nationalities', label: "Nationalities", sortable: true, class: 'tableWidthMedium'},
                        {key: 'gender', value: 'gender', sortable: true, class: 'tableWidthSmall'},
                        {key: 'age', value: 'age', sortable: true, class: 'tableWidthSmall'},
                        {key: 'travellerType', label: "Traveller Types", sortable: true, class: 'tableWidthMedium'},
                        {key: 'actions', class: 'tableWidthMedium'}]
                }
                return [
                    {key: 'profilePhoto', label: "Photo", sortable: true, class: 'tableWidthSmall'},
                    {key: 'firstName', label: "First Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'lastName', label: "Last Name", sortable: true, class: 'tableWidthSmall'},
                    {key: 'actions', class: 'tableWidthMedium'}]
            }
        },

        methods: {
            /**
             * Used to calculate a specific rows nationalities from their list of nationalities. Shows all the
             * nationalities in the row.
             *
             * @param nationalities     the row's (profile) nationalities.
             */
            calculateNationalities(nationalities) {
                let nationalityList = "";
                for (let i = 0; i < nationalities.length; i++) {
                    if (nationalities[i + 1] !== undefined) {
                        nationalityList += nationalities[i].nationality + ", ";
                    } else {
                        nationalityList += nationalities[i].nationality;
                    }

                }
                return nationalityList;
            },


            /**
             * Used to calculate a specific rows traveller types from their list of traveller types. Shows all the
             * traveller types in the row.
             *
             * @param travellerTypes     the row's (profile) traveller types.
             */
            calculateTravTypes(travellerTypes) {
                let travTypeList = "";
                for (let i = 0; i < travellerTypes.length; i++) {
                    if (travellerTypes[i + 1] !== undefined) {
                        travTypeList += travellerTypes[i].travellerType + ", ";
                    } else {
                        travTypeList += travellerTypes[i].travellerType;
                    }

                }
                return travTypeList;
            },


            /**
             * Method to make a user an admin. This method is only available if the currently logged in user is an
             * admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be made an admin.
             */
            makeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/makeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Method to remove admin attribute from a user. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param makeAdminProfile      the selected profile to be removed as an admin.
             */
            removeAdmin(makeAdminProfile) {
                let self = this;
                fetch('/v1/removeAdmin/' + makeAdminProfile.id, {
                    method: 'POST',
                }).then(function () {
                    self.searchProfiles();
                }).then(function () {
                    if (self.profile.id === makeAdminProfile.id) {
                        self.$router.push("/dash");
                        self.$router.go();
                    }
                })
            },


            /**
             * Method to delete a user's profile. This method is only available if the currently logged in
             * user is an admin. Backend validation ensures a user cannot bypass this.
             *
             * @param deleteUser      the selected profile to be deleted.
             */
            deleteUser(deleteUser) {
                let self = this;
                fetch('/v1/profile/' + deleteUser.id, {
                    method: 'DELETE',
                }).then(function () {
                    self.searchProfiles();
                })
            },


            /**
             * Changes fields so that they can be used in searching.
             * Runs validation on range fields.
             */
            searchProfiles(searchParameters) {
                searchParameters.minAge = parseInt(searchParameters.minAge);
                searchParameters.maxAge = parseInt(searchParameters.maxAge);
                if (isNaN(searchParameters.minAge) || isNaN(searchParameters.maxAge)) {
                    this.showError = true;
                    this.alertMessage = "Min or Max Age are not numbers";
                } else if (searchParameters.minAge > searchParameters.maxAge) {
                    this.showError = true;
                    this.alertMessage = "Min age is greater than max age";
                } else {
                    this.showError = false;
                    this.queryProfiles(searchParameters);
                }
            },


            /**
             * Queries database for profiles which fit search criteria.
             */
            queryProfiles(searchParameters) {
                this.retrievingProfiles = true;
                let searchQuery =
                    "?nationalities=" + searchParameters.nationality +
                    "&gender=" + searchParameters.gender +
                    "&min_age=" + searchParameters.minAge +
                    "&max_age=" + searchParameters.maxAge +
                    "&travellerTypes=" + searchParameters.travellerType;
                return fetch(`/v1/profiles` + searchQuery, {})
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.retrievingProfiles = false;
                        this.profiles = data;
                    })
            },


            /**
             * Displays default image when no image is found.
             * @param event     image error event.
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_profile_picture.png"
            },


            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             *
             * @param response, passed back to the getAllTrips function to be parsed into a Json.
             * @returns throws the error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                this.showError = true;
                response.clone().text().then(text => {
                    this.alertMessage = text;
                });
                console.log(error); // eslint-disable-line no-console
                throw error;
            },


            /**
             * Used to turn the response of the fetch method into a usable Json.
             *
             * @param response of the fetch method.
             * @returns the Json body of the response.
             */
            parseJSON(response) {
                return response.json();
            },


            /**
             * Used to send a selected profile to a modal so the admin can confirm they want to delete the selected
             * profile.
             *
             * @param profile, the profile that is selected to be deleted.
             */
            sendProfileToModal(profile) {
                this.selectedProfile = profile;
            },


            /**
             * Used to dismiss the delete a profile modal.
             */
            dismissModal() {
                this.$refs['deleteProfileModal'].hide();
            },


            /**
             * Emits the event that the admin is viewing a profile to edit, this will navigate to the edit page.
             */
            emitAdminEdit(profile) {
                this.$emit('admin-edit', profile);
            }

        }
    }
</script>

<style>
    .tableWidthSmall {
        max-width: 8%;
        width: 8%;
        text-align: center;
    }

    .tableWidthMedium {
        max-width: 8%;
        width: 15%;
        text-align: center;
    }

    .tableWidthLarge {
        max-width: 8%;
        width: 25%;
        text-align: center;
    }
</style>