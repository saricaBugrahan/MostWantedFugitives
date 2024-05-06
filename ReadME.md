# Certificate setup

This project aims to extract the fugitives information from selenium. The structure will be as follows:

<ol>
  <li>Scrape docker image scraping fugitive information from www.terorarananlar.pol.tr</li>
  <li>Scrape docker image sends the image to the RabbitMQ structure</li>
  <li>Server docker image receives image via RabbitMQ</li>
  <li>Then, it will create an ElasticSearch or (database) and saves into it</li>
  <li>User will interact via API and get the responses</li>
</ol>
div.deactivated-list-card:nth-child(1)