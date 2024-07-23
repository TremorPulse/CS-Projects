# Use a Node.js parent image
FROM node:14

# Set the working directory
WORKDIR /app

# Copy package.json and possibly package-lock.json if you have it
COPY package.json package-lock.json /app/

# Install dependencies using npm
RUN npm install

# Bundle app source
COPY . /app

# Build the project for production
RUN npm run build

# Install serve to serve the app on container startup
# If you use `serve`, you need to install it, or adjust according to your server setup
RUN npm install -g serve
CMD ["serve", "-s", "build", "-l", "3000"]
