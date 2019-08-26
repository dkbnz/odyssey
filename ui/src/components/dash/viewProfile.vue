<template>
    <div class="scroll">
        <div class="wrapper cf">
            <div :class="containerClass">
                <!-- The profile picture of the current profile being viewed. -->
                <b-img :src="profileImageThumb" fluid rounded="circle" thumbnail
                       @click="showImage" onerror="this.src = '../../../static/default_profile_picture.png'">
                </b-img>
                <b-alert
                        class="m-1"
                        :show="dismissCountDown"
                        dismissible
                        variant="success"
                        @dismissed="dismissCountDown=0"
                        @dismiss-count-down="countDownChanged"
                > {{alertMessage}}
                </b-alert>
                <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>
                <h1>{{profile.firstName}} {{profile.middleName}} {{profile.lastName}}</h1>
                <p v-if="profile.admin"><i>Administrator</i></p>
                <p v-else><i>Regular User</i></p>
                <h2>Personal Details</h2>
                <p> Username: {{ profile.username }}</p>
                <p> Date of Creation: {{ new Date(profile.dateOfCreation).toUTCString()}}</p>
                <p> Date of Birth: {{new Date(profile.dateOfBirth).toLocaleDateString()}}</p>
                <p> Gender: {{ profile.gender }}</p>
                <h2> Nationalities </h2>
                <ul>
                    <li v-for="nationality in profile.nationalities">{{ nationality.nationality }}</li>
                </ul>

                <h2> Passports </h2>
                <ul>
                    <li v-for="passport in profile.passports">{{ passport.country }}</li>
                </ul>

                <h2> Traveller Types </h2>
                <ul>
                    <li v-for="travType in profile.travellerTypes">{{ travType.travellerType }}</li>
                </ul>
            </div>
            <div :class="containerClassContent">
                <div :class="'containerWithNav'">
                    <h1 class="page-title">Quests</h1>
                    <p class="page-title"><i>Click a quest below to add it to your list of quests.</i></p>
                    <active-quest-list
                            :quest-attempts="questAttempts"
                            :loading-results="loadingResults"
                            @quest-attempt-clicked="showAddQuestAttempt">
                    </active-quest-list>
                    <b-modal id="modal-selected-quest" centered ref="selected-quest-modal">
                        <div v-if="selectedQuest" slot="modal-title" class="mb-1">
                            {{selectedQuest.title}}
                        </div>
                        <div v-if="selectedQuest">

                            <div class="d-flex w-100 justify-content-center">
                                <p>{{new Date(selectedQuest.startDate).toLocaleDateString()}} &rarr;
                                    {{new Date(selectedQuest.endDate).toLocaleDateString()}}</p>
                            </div>
                        </div>
                        <template slot="modal-footer">
                            <b-col>
                                <b-button @click="$refs['selected-quest-modal'].hide()" block>
                                    Close
                                </b-button>
                            </b-col>
                            <b-col>
                                <b-button variant="primary"
                                          @click="addQuestToProfile" block>Add to Your Quests
                                </b-button>
                            </b-col>
                        </template>

                    </b-modal>
                </div>

                <!-- Displays the profile's photo gallery -->
                <photo-gallery :key="refreshPhotos"
                               :profile="profile"
                               :userProfile="userProfile"
                               :adminView="adminView"
                               @makeProfilePhoto="setProfilePhoto"
                               @removePhoto="refreshProfilePicture"
                               class="d-none d-lg-block">
                </photo-gallery>
                <!-- Displays a profile's trips -->
                <your-trips :adminView="adminView"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="userProfile"
                            class="d-none d-lg-block">
                </your-trips>
            </div>
            <b-modal hide-footer centered ref="profilePictureModal" title="Profile Picture" size="xl">
                <b-img-lazy :src="profileImageFull" onerror="this.src = '../../../static/default_profile_picture.png'"
                            center fluid></b-img-lazy>
                <b-row>
                    <b-col>
                        <b-button
                                block class="mr-2"
                                size="sm" style="margin-top: 10px"
                                v-if="authentication" variant="info"
                                @click="showUploader">Change my profile picture
                        </b-button>
                        <b-modal ref="profilePhotoUploader" id="profilePhotoUploader" hide-footer centered
                                 title="Change Profile Photo">
                            <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
                            <photoUploader @save-photos="uploadProfilePhoto"
                                           :acceptTypes="'image/jpeg, image/jpg, image/png'"
                                           :multipleFiles="false">
                            </photoUploader>
                        </b-modal>
                    </b-col>
                    <b-col>
                        <b-button
                                @click="deleteProfilePhoto"
                                block class="mr-2"
                                size="sm" style="margin-top: 10px"
                                v-if="authentication && profile.profilePicture !== null"
                                variant="danger">Remove as Profile Photo
                        </b-button>
                    </b-col>
                </b-row>
            </b-modal>

        </div>
    </div>

</template>

<script>
    import YourTrips from "../trips/yourTrips.vue"
    import PhotoGallery from "../photos/photoGallery";
    import PhotoUploader from "../photos/photoUploader";
    import QuestList from "../quests/questList";
    import ActiveQuestList from "../quests/activeQuestList";

    export default {
        name: "viewProfile",

        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile;
                }
            },
            nationalityOptions: {
                default: function () {
                    return [];
                }
            },
            travTypeOptions: {
                default: function () {
                    return [];
                }
            },
            trips: {
                default: function () {
                    return [];
                }
            },
            adminView: Boolean,
            destinations: {
                default: function () {
                    return [];
                }
            },
            showSaved: {
                default: function () {
                    return false;
                }
            },
            containerClass: {
                default: function () {
                    return 'sidebar'
                }
            },
            containerClassContent: {
                default: function () {
                    return 'content'
                }
            }
        },

        data() {
            return {
                authentication: false,
                showSuccess: false,
                showError: false,
                alertMessage: "",
                profileImageThumb: "",
                profileImageFull: "",
                errorMessage: "",
                newProfilePhoto: -1,
                refreshPhotos: 0,
                dismissSecs: 3,
                dismissCountDown: 0,
                questAttempts: [],
                loadingResults: false,
                selectedQuest: null
            }
        },

        watch: {
            userProfile() {
                this.checkAuthentication();
            },

            profile() {
                this.queryYourActiveQuests();
            }
        },

        mounted() {
            this.checkAuthentication();
            this.getProfilePictureThumbnail();
            this.getProfilePictureFull();
            this.queryYourActiveQuests();
        },

        methods: {
            /**
             * Displays the default profile picture.
             */
            showImage() {
                this.$refs['profilePictureModal'].show();
            },


            /**
             * Emits change up to view profile be able to auto update front end when changing profile picture
             */
            makeProfileImage() {
                let self = this;

                fetch('/v1/profilePhoto/' + this.newProfilePhoto.id, {
                    method: 'PUT'
                }).then(response => {
                    if (response.status === 200) {
                        self.showError = false;
                        self.setProfilePhoto(self.newProfilePhoto.id);
                        self.newProfilePhoto.public = true;
                        self.profile.photoGallery.push(self.newProfilePhoto);
                        self.refreshPhotos += 1;
                        self.$refs['profilePictureModal'].hide();
                        self.$refs['profilePhotoUploader'].hide();
                    } else {
                        self.showError = true;
                        self.alertMessage = "An error occurred when making this your profile photo";
                    }
                });
            },


            /**
             * Creates the POST request for directly uploading a new profile photo.
             *
             * @files   the files containing the new profile photo.
             */
            uploadProfilePhoto(files) {
                let self = this;
                fetch(`/v1/photos/` + this.profile.id, {
                    method: 'POST',
                    body: this.getFormData(files)

                }).then(function (response) {
                    if (response.status === 201) {
                        response.clone().json().then(text => {
                            self.newProfilePhoto = text[text.length - 1];
                            self.makeProfileImage();
                        });
                    }
                })
            },


            /**
             * Creates the form data to send as the body of the POST request to the backend.
             *
             * @param files         the photo uploaded from the personal photos component.
             * @returns {FormData}  the FormData stringified for use in the POST request.
             */
            getFormData(files) {
                let personalPhotos = new FormData();
                personalPhotos.append('photo0', files);
                return personalPhotos;
            },


            /**
             * Retrieves a Json body from a response.
             *
             * @param response  the response parsed into Json.
             * @returns {*}     the Json response body.
             */
            parseJSON(response) {
                if (response.status === 201) {
                    this.files = null;
                    this.$refs['profilePhotoUploader'].hide();
                } else {
                    this.showError = true;
                    this.errorMessage = "Invalid image size/type"
                }
                return response.json();
            },


            /**
             * Display the modal for uploading a single profile photo.
             */
            showUploader() {
                this.showError = false;
                this.$refs.profilePhotoUploader.show();
            },


            /**
             * Checks the authorization of the user profile that is logged in to see if they can
             * view the users private photos and can add or delete images from the media.
             */
            checkAuthentication() {
                this.authentication = (this.userProfile.id !== undefined) && (this.userProfile.id === this.profile.id) || (this.userProfile.admin && this.adminView);
            },


            /**
             * Retrieves the user's primary photo thumbnail, if none is found set to the default image.
             */
            getProfilePictureThumbnail() {
                if (this.profile.profilePicture !== null && this.profile.profilePicture !== undefined) {
                    this.profileImageThumb = `/v1/photos/thumb/` + this.profile.profilePicture.id;
                } else {
                    this.profileImageThumb = "../../../static/default_profile_picture.png";
                }
            },


            /**
             * Retrieves the user's primary photo, if none is found set to the default image.
             */
            getProfilePictureFull() {
                if (this.profile.profilePicture !== null && this.profile.profilePicture !== undefined) {
                    this.profileImageFull = `/v1/photos/` + this.profile.profilePicture.id;
                } else {
                    this.profileImageFull = "../../../static/default_profile_picture.png";
                }
            },


            /**
             * Changes the profile picture on front end instead of needing the refresh page when adding a new
             * image from your photo gallery
             */
            setProfilePhoto(photoId) {
                this.profileImageThumb = `/v1/photos/thumb/` + photoId;
                this.profileImageFull = `/v1/photos/` + photoId;
                this.profile.profilePicture = {"id": photoId, "public": true}
            },


            /**
             * Deletes the user's profile photo and sets it back to the default image.
             */
            deleteProfilePhoto() {
                let self = this;
                fetch('/v1/profilePhoto/' + this.profile.id, {
                    method: 'DELETE'
                }).then(function (response) {
                    if (response.status === 200) {
                        self.profileImageThumb = "../../../static/default_profile_picture.png";
                        self.profileImageFull = "../../../static/default_profile_picture.png";
                        self.profile.profilePicture = null;
                        self.showAlert();
                        self.alertMessage = "Profile photo successfully deleted";
                        self.$refs['profilePictureModal'].hide();
                    } else {
                        self.showError = true;
                        self.alertMessage = "Unable to delete profile photo";
                    }
                })
            },


            /**
             * Handles refreshing of a profile picture upon deleting of a photo from the photo gallery. Pops the photo
             * from the list of photos in the photo gallery (so page doesn't need to refresh) and if the profile picture
             * was deleted, sets it back to the default.
             *
             * @param photoId the id number of the photo that was deleted.
             */
            refreshProfilePicture(photoId) {
                for (let i = 0; i < this.profile.photoGallery.length; i++) {
                    if (this.profile.photoGallery[i].id === photoId) {
                        if (i + 1 === this.profile.photoGallery.length) {
                            this.profile.photoGallery.pop();
                        } else {
                            this.profile.photoGallery[i] = this.profile.photoGallery[i + 1];
                        }
                    }
                }
                if (this.profile.profilePicture !== null && photoId === this.profile.profilePicture.id) {
                    this.profileImageThumb = "../../../static/default_profile_picture.png";
                    this.profileImageFull = "../../../static/default_profile_picture.png";
                    this.profile.profilePicture = null;
                }
            },


            /**
             * Starts the countdown for the profile successfully saved alert.
             *
             * @param dismissCountDown the timer for the alert countdown.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },


            /**
             * Displays the alert for a profile successfully saved.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },


            /**
             * Displays default image when no image is found.
             *
             * @param event     image error event.
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_profile_picture.png"
            },


            /**
             * Runs a query which searches through the quests in the database and returns only
             * quests started by the profile.
             *
             * @returns {Promise<Response | never>}
             */
            queryYourActiveQuests() {
                if (this.profile.id !== undefined) {
                    this.loadingResults = true;
                    return fetch(`/v1/quests/profiles/` + this.profile.id, {})
                        .then(response => response.json())
                        .then((data) => {
                            this.questAttempts = data;
                            this.loadingResults = false;
                        })
                }
            },


            /**
             * Shows the modal to add the quest to your list of quests only if you are not looking at your own profile.
             *
             * @param questAttempt  the quest attempt containing the quest to be added.
             */
            showAddQuestAttempt(questAttempt) {
                if (this.userProfile.id !== this.profile.id) {
                    this.selectedQuest = questAttempt.questAttempted;
                    this.$refs['selected-quest-modal'].show();
                }

            },


            /**
             * Sends the request to add the selected quest to your quest attempts. Show's toasts on error/success.
             *
             * @returns {Promise<Response | never>}    the Fetch method promise.
             */
            addQuestToProfile() {
                if (this.userProfile.id !== undefined) {
                    return fetch(`/v1/quests/` + this.selectedQuest.id + `/attempt/` + this.userProfile.id, {
                        method: 'POST'
                    }).then(response => {
                        if (response.ok) {
                            // Display 'created' toast
                            this.$bvToast.toast('Quest added to active', {
                                title: `Quest Added`,
                                variant: "default",
                                autoHideDelay: "3000",
                                solid: true
                            });
                        } else {
                            this.$bvToast.toast('Something went wrong', {
                                title: `Quest Adding Failed`,
                                variant: "danger",
                                autoHideDelay: "3000",
                                solid: true
                            });
                        }
                    })
                }
            }
        },

        components: {
            ActiveQuestList,
            QuestList,
            YourTrips,
            PhotoGallery,
            PhotoUploader
        }
    }
</script>
