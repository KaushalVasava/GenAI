# 📸 Image Description for Android (ML Kit GenAI)

Automatically generate natural-language descriptions for images in your Android app using Google ML Kit’s GenAI Image Description API — an on-device generative AI that turns pictures into human-readable captions.

This feature elevates accessibility (e.g., alt text for visually impaired users), enhances search metadata, and brings AI intelligence to your app without cloud costs or manual model hosting.
Google for Developers

# 🚀 Features

✔ Generate short, human-friendly text descriptions for any image
✔ On-device inference using ML Kit’s GenAI model
✔ Works with Bitmap images captured from camera or gallery
✔ No internet or cloud API keys required
✔ Useful for alt text, tagging, search, or visual summarization

# 🧠 How It Works

Under the hood, ML Kit’s Image Description API uses an optimized generative model (powered by Gemini Nano) that analyzes bitmap input and produces a concise caption describing its contents.
Google for Developers

# 📦 Installation

Add the ML Kit Image Description dependency in your module build.gradle:

dependencies {
    implementation("com.google.mlkit:genai-image-description:1.0.0-beta1")
}


⚠️ Requires Android API level 26+. Model downloads are handled on-device as needed.
Google for Developers

# 🧩 Usage (Kotlin)
1. Create the Image Describer Client
val options = ImageDescriberOptions.builder(context).build()
val imageDescriber = ImageDescription.getClient(options)

2. Check Feature Availability

Before running inference, check if the feature is downloadable/available:

val status = imageDescriber.checkFeatureStatus().await()


Handle states like:

UNAVAILABLE

DOWNLOADABLE → download before use

AVAILABLE → ready for inference

3. Prepare the Image and Run Inference
val request = ImageDescriptionRequest.builder(bitmap).build()

// Streaming callback
imageDescriber.runInference(request) { text ->
    // Append or handle generated text description
}

// Or non-streaming
val result = imageDescriber.runInference(request).await()
val description = result.description

4. Cleanup

Always close the client when done:

imageDescriber.close()


Model download and feature availability are managed automatically but require network only once.
Google for Developers

# 🧠 Use Cases

✨ Accessibility: Generate alt text to help visually impaired users understand images.
Google for Developers

🔍 Image Metadata: Use descriptions for indexing, search, or categorization.
Google for Developers

🗣️ Voice-first UI: Show or speak image content while driving or when screen isn’t visible.
Google for Developers

# ⚠️ Limitations

Currently supports only English output.
Google for Developers

Output is a single short description per image.
Google for Developers

Device variations may affect model availability and performance.
Google for Developers

# 🧪 Example Result
Input Image	Generated Description
Dog running in a field	“A small white dog with a black nose runs on grass near a bridge.”
💡 Tips

✔ Always wrap inference calls in coroutine scopes to avoid blocking UI.
✔ Use image rotation metadata when creating the input bitmap.
✔ Check checkFeatureStatus() each app launch to ensure model readiness.

# 📘 Inspiration

This feature is part of Google’s new on-device GenAI APIs that give Android apps powerful AI features like summarization, rewriting, and image descriptions — all without cloud infrastructure

# Contribution
You can contribute this project. Just Solve issue or update code and raise PR. I'll do code review and merge your changes into main branch.

See Commit message guidelines https://initialcommit.com/blog/git-commit-messages-best-practices.
Guidlines to create pull request [feature_name]_#your_nickname this should be the branch name.
Licence
Copyright 2023 Kaushal Vasava

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

# Author
Kaushal Vasava

# Thank you
Contact us if you have any query on LinkedIn, Github, Twitter or Email: kaushalvasava.app.feedback@gmail.com
