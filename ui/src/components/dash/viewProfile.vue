<template>
    <div class="scroll">
        <div class="wrapper cf">
            <div :class="containerClass">
                <b-img :src="profileImageThumb" fluid rounded="circle" thumbnail
                       @click="showImage">
                </b-img>
                <b-alert dismissible v-model="showSuccess" variant="success">{{alertMessage}}</b-alert>
                <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>
                <h1>{{profile.firstName}} {{profile.middleName}} {{profile.lastName}}</h1>
                <p v-if="profile.isAdmin"><i>Administrator</i></p>
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
                <photo-gallery :profile="profile" :userProfile="userProfile" :adminView="adminView"></photo-gallery>
                <!-- Displays a profile's trips -->
                <your-trips :adminView="adminView"
                            :destinations="destinations"
                            :profile="profile"
                            :userProfile="userProfile">
                </your-trips>
            </div>
            <b-modal hide-footer centered ref="profilePictureModal" title="Profile Picture" size="xl">
                <b-img-lazy :src="profileImageFull" fluid></b-img-lazy>
                <b-row>
                    <b-col>
                        <b-button
                                block class="mr-2"
                                size="sm" style="margin-top: 10px"
                                v-if="auth" variant="info"
                                @click="showUploader" >Change my profile picture
                        </b-button>
                        <b-modal ref="profilePhotoUploader" id="profilePhotoUploader" hide-footer centered
                                 title="Change Profile Photo">
                            <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
                            <photoUploader @save-photos="uploadProfilePhoto"
                                           :acceptTypes="'image/jpeg, image/jpg, image/png'"
                                           :multipleFiles="false"></photoUploader>
                        </b-modal>
                    </b-col>
                    <b-col>
                        <b-button
                                @click="deleteProfilePhoto"
                                block class="mr-2"
                                size="sm" style="margin-top: 10px"
                                v-if="auth && profile.profilePicture !== null"
                                variant="danger">Delete
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

    export default {
        name: "viewProfile",
        props: {
            profile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            trips: Array,
            userProfile: {
                default: function() {
                    return this.profile;
                }
            },
            adminView: Boolean,
            destinations: Array,
            showSaved: {
                default: function () {
                    return false;
                }
            },
            containerClass: {
                default: function() {
                    return 'sidebar'
                }
            },
            containerClassContent: {
                default: function() {
                    return 'content'
                }
            }
        },
        data() {
            return {
                auth: false,
                showSuccess: false,
                showError: false,
                alertMessage: "",
                profileImageThumb: "",
                profileImageFull: "",
                errorMessage: "",
                newProfilePhoto: -1
            }
        },
        mounted() {
            this.checkAuth();
            this.getProfilePictureThumbnail();
            this.getProfilePictureFull();
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
                        self.setProfilePhoto(this.newProfilePhoto.id);
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
                            self.newProfilePhoto = text[text.length -1];
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
             * Retrieves a json body from a response.
             *
             * @param response      The response parsed into json.
             * @returns {*}
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
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id) || (this.userProfile.isAdmin && this.adminView);
            },

            /**
             * Retrieves the user's primary photo thumbnail.
             */
            getProfilePictureThumbnail() {
                if (this.profile.profilePicture !== null) {
                    this.profileImageThumb = `/v1/photos/thumb/` + this.profile.profilePicture.id;
                }
                return "../../../static/default_profile_picture.png";

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
             * Retrieves the user's primary photo.
             */
            getProfilePictureFull() {
                if (this.profile.profilePicture !== null) {
                    this.profileImageFull = `/v1/photos/` + this.profile.profilePicture.id;
                }
                return "../../../static/default_profile_picture.png";
            },

            /**
             * Deletes the user's profile photo.
             */
            deleteProfilePhoto() {
                let self = this;
                fetch('/v1/profilePhoto/' + this.profile.id, {
                    method: 'DELETE'
                }).then(function(response) {
                    if(response.status === 200) {
                        self.profileImageThumb = "../../../static/default_profile_picture.png";
                        self.profileImageFull = "../../../static/default_profile_picture.png";
                        self.profile.profilePicture = null;
                        self.showSuccess = true;
                        self.alertMessage = "Profile photo successfully deleted";
                        self.$refs['profilePictureModal'].hide();
                    } else {
                        self.showError = true;
                        self.alertMessage = "Unable to delete profile photo";
                    }
                })
            }
        },
        components: {
            YourTrips,
            PhotoGallery,
            PhotoUploader
        }
    }
</script>
