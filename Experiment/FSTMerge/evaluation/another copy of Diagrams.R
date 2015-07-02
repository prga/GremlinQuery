###################################
# Input data
###################################

#load tables
#csharp
NancySM <- read.table("./Nancy/result-semiStructured.csv", sep="\t", header=TRUE)
NancyUM <- read.table("./Nancy/result-unStructured.csv", sep="\t", header=TRUE)

ReactiveUISM <- read.table("./ReactiveUI/result-semiStructured.csv", sep="\t", header=TRUE)
ReactiveUIUM <- read.table("./ReactiveUI/result-unStructured.csv", sep="\t", header=TRUE)

SignalRSM <- read.table("./SignalR/result-semiStructured.csv", sep="\t", header=TRUE)
SignalRUM <- read.table("./SignalR/result-unStructured.csv", sep="\t", header=TRUE)

#python
flaskSM <- read.table("./flask/result-semiStructured.csv", sep="\t", header=TRUE)
flaskUM <- read.table("./flask/result-unStructured.csv", sep="\t", header=TRUE)

requestsSM <- read.table("./requests/result-semiStructured.csv", sep="\t", header=TRUE)
requestsUM <- read.table("./requests/result-unStructured.csv", sep="\t", header=TRUE)

scrapySM <- read.table("./scrapy/result-semiStructured.csv", sep="\t", header=TRUE)
scrapyUM <- read.table("./scrapy/result-unStructured.csv", sep="\t", header=TRUE)

#java
mockitoSM <- read.table("./mockito/result-semiStructured.csv", sep="\t", header=TRUE)
mockitoUM <- read.table("./mockito/result-unStructured.csv", sep="\t", header=TRUE)

dropwizardSM <- read.table("./dropwizard/result-semiStructured.csv", sep="\t", header=TRUE)
dropwizardUM <- read.table("./dropwizard/result-unStructured.csv", sep="\t", header=TRUE)

nettySM <- read.table("./netty/result-semiStructured.csv", sep="\t", header=TRUE)
nettyUM <- read.table("./netty/result-unStructured.csv", sep="\t", header=TRUE)

RxJavaSM <- read.table("./RxJava/result-semiStructured.csv", sep="\t", header=TRUE)
RxJavaUM <- read.table("./RxJava/result-unStructured.csv", sep="\t", header=TRUE)

###################################
# Global variables
###################################

SM <- "semistructured"
UM <- "unstructured"

# color of semistructured lines
COLSM <- "green"
#COLSM <- "gray95"

# color of unstructured lines
COLUM <- "red"
#COLUM <- "gray25"

# Pointtype
POINTTYPE <- "o"
POINTCH <- 20

###################################
# Functions
###################################

#builds dataframe from csv-input files
buildDataFrame <- function(project, semistructuredTable, unstructuredTable)
{
	SMRevisions <- semistructuredTable$Revisions
	UMRevisions <- unstructuredTable$Revisions
	
	SMconflicts <- semistructuredTable$Syntactic.Conflicts
	UMconflicts <- unstructuredTable$Syntactic.Conflicts	

	SMConfLines <- semistructuredTable$Confilicting.Lines.1 + semistructuredTable$Conflicting.Lines.2
	UMConfLines <- unstructuredTable$Confilicting.Lines.1   + unstructuredTable$Conflicting.Lines.2	

	SMfiles <- semistructuredTable$Number.of.Files
	UMfiles <- unstructuredTable$Number.of.Files
	
	SMsemConf <- semistructuredTable$Semantic.Conflicts
	UMsemConf <- unstructuredTable$Semantic.Conflicts

	dataFrameSM <- data.frame(as.factor(project), as.character(SMRevisions), 
	                          as.numeric(SMconflicts), as.numeric(SMConfLines), 
	                          as.numeric(SMfiles), as.factor(SM),
	                          as.numeric(SMsemConf))
	colnames(dataFrameSM) <- c("Programm", "Revisions", 
	                           "Conf", "ConfLines", 
	                           "ConfFiles", "Merge",
	                           "SemConf")

	dataFrameUM <- data.frame(as.factor(project), as.character(UMRevisions),
	                          as.numeric(UMconflicts), as.numeric(UMConfLines), 
	                          as.numeric(UMfiles),  as.factor(UM),
	                          as.numeric(UMsemConf))

	colnames(dataFrameUM) <- c("Programm", "Revisions", 
	                           "Conf", "ConfLines", 
	                           "ConfFiles", "Merge",
	                           "SemConf")

	dataFrame <- rbind(dataFrameSM, dataFrameUM)

	return(dataFrame)
}

# draw a conflict bar plot
drawConflictsBarPlot <- function(projectName, dataFrame, legendposition)
{	
	barplotFrameSM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==SM,select=Conf)
	names(barplotFrameSM)[names(barplotFrameSM)=="Conf"] <- "Conf-SM"
	
	barplotFrameUM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==UM,select=Conf)
	names(barplotFrameUM)[names(barplotFrameUM)=="Conf"] <- "Conf-UM"
		
	barplotFrame <- cbind(barplotFrameSM, barplotFrameUM)

	barplotMatrix <- as.matrix(barplotFrame)
	barplotMatrix <- t(barplotMatrix)
	revs <- subset(dataFrame,dataFrame$Programm==projectName,select=Revisions)

	mp <- barplot(barplotMatrix, beside = TRUE, axisnames=FALSE, col=c(COLSM,COLUM))

	#draw axis
	axis(1, at=mp[1, ], labels=FALSE, tick=FALSE)
	labels <- paste(revs$Revisions)
	text(mp[1, ], par("usr")[3]-0.05, srt=-60, adj=c(-0.15,0), labels=labels, xpd=TRUE, cex=1) 
	
	#draw grid
	grid(lty="dotdash", col="darkgrey")	

	
	# draw legend
	drawLegendBarPlot(legendposition)

	# draw title
	title(ylab="Number of conflicts",cex.lab=1)
}

drawConflictLinesBarPlot <- function(projectName, dataFrame, legendposition)
{
	
	barplotFrameSM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==SM,select=ConfLines)
	names(barplotFrameSM)[names(barplotFrameSM)=="ConfLines"] <- "ConfLines-SM"
	
	barplotFrameUM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==UM,select=ConfLines)
	names(barplotFrameUM)[names(barplotFrameUM)=="ConfLines"] <- "ConfLines-UM"
	
	barplotFrame <- cbind(barplotFrameSM, barplotFrameUM)
	
	barplotMatrix <- as.matrix(barplotFrame)
	barplotMatrix <- t(barplotMatrix)
	revs <- subset(dataFrame,dataFrame$Programm==projectName,select=Revisions)
	
	mp <- barplot(barplotMatrix, beside = TRUE, axisnames=FALSE, col=c(COLSM,COLUM))

	#draw axis
	axis(1, at=mp[1, ], labels=FALSE, tick=FALSE)
	labels <- paste(revs$Revisions)
	text(mp[1, ], par("usr")[3]-0.05, srt=-60, adj=c(-0.15,0), labels=labels, xpd=TRUE, cex=1) 
	
	#draw grid
	grid(lty="dotdash", col="darkgrey")	
	
	# draw legend
	drawLegendBarPlot(legendposition)

	# draw title
	title(ylab="Number of conflicting lines of code",cex.lab=1)	
}

drawConflictFilesBarPlot <- function(projectName, dataFrame, legendposition)
{
	
	barplotFrameSM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==SM,select=ConfFiles)
	names(barplotFrameSM)[names(barplotFrameSM)=="ConfFiles"] <- "ConfFiles-SM"
	
	barplotFrameUM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==UM,select=ConfFiles)
	names(barplotFrameUM)[names(barplotFrameUM)=="ConfFiles"] <- "ConfFiles-UM"
	
	barplotFrame <- cbind(barplotFrameSM, barplotFrameUM)
	
	barplotMatrix <- as.matrix(barplotFrame)
	barplotMatrix <- t(barplotMatrix)
	revs <- subset(dataFrame,dataFrame$Programm==projectName,select=Revisions)
	
	mp <- barplot(barplotMatrix, beside = TRUE, axisnames=FALSE, col=c(COLSM,COLUM))

	#draw axis
	axis(1, at=mp[1, ], labels=FALSE, tick=FALSE)
	labels <- paste(revs$Revisions)
	text(mp[1, ], par("usr")[3]-0.05, srt=-60, adj=c(-0.15,0), labels=labels, xpd=TRUE, cex=1) 

	
	#draw grid
	grid(lty="dotdash", col="darkgrey")	

	
	# draw legend
	drawLegendBarPlot(legendposition)

	# draw title
	title(ylab="Number of conflicting files",cex.lab=1)
}


drawConflictSemanticBarPlot <- function(projectName, dataFrame, legendposition)
{
	
	barplotFrameSM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==SM,select=SemConf)
	names(barplotFrameSM)[names(barplotFrameSM)=="SemConf"] <- "SemConf-SM"
	
	barplotFrameUM <- subset(dataFrame,dataFrame$Programm==projectName& dataFrame$Merge==UM,select=SemConf)
	names(barplotFrameUM)[names(barplotFrameUM)=="SemConf"] <- "SemConf-UM"
	
	barplotFrame <- cbind(barplotFrameSM, barplotFrameUM)
	
	barplotMatrix <- as.matrix(barplotFrame)
	barplotMatrix <- t(barplotMatrix)
	revs <- subset(dataFrame,dataFrame$Programm==projectName,select=Revisions)
	
	mp <- barplot(barplotMatrix, beside = TRUE, axisnames=FALSE, col=c(COLSM,COLUM))

	#draw axis
	axis(1, at=mp[1, ], labels=FALSE, tick=FALSE)
	labels <- paste(revs$Revisions)
	text(mp[1, ], par("usr")[3]-0.05, srt=-60, adj=c(-0.15,0), labels=labels, xpd=TRUE, cex=1) 

	
	#draw grid
	grid(lty="dotdash", col="darkgrey")	

	
	# draw legend
	drawLegendBarPlot(legendposition)

	# draw title
	title(ylab="Number of semantic conflicts",cex.lab=1)
}


# function for axis and grid
drawCaption <- function(table)
{
	# draw revision axis
	axis(	1, 
		at = table$Revisions, 
		labels = FALSE, 
		tick = TRUE
	)
	
	# labels for xaxis
	labels <- paste(table$Revisions)
	
	# draw labels on xaxis
	text(	
		table$Revisions, 
		par("usr")[3], 
		srt = -60, 
		labels = labels, 
		adj = c(-0.1,1), 
		xpd=TRUE,
		cex=1
	)
	
	#draw grid
	grid(lty="dotdash", col="darkgrey")	
}


drawLegendBarPlot <- function(position)
{
 legend(position,                             
         legend=c("Semistructured Merge","Unstructured Merge"),
         cex=0.7,            
         fill=c(COLSM,COLUM),
         bg="white")  
}



drawBarplotPNGs <- function()
{

WIDTH <- 640
HEIGHT <- 480

#csharp
#Nancy
png(filename="./Nancyc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("Nancy", Nancydf, "topleft")
dev.off()

png(filename="./Nancycl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("Nancy", Nancydf, "topleft")
dev.off()

png(filename="./Nancycf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("Nancy", Nancydf, "topleft")
dev.off()

png(filename="./Nancysc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("Nancy", Nancydf, "topright")
dev.off()


#ReactiveUI
png(filename="./ReactiveUIc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("CruiseControl.NET", ReactiveUIdf, "topright")
dev.off()

png(filename="./ReactiveUIcl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("CruiseControl.NET", ReactiveUIdf, "topright")
dev.off()

png(filename="./ReactiveUIcf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("CruiseControl.NET", ReactiveUIdf, "topright")
dev.off()

png(filename="./ReactiveUIsc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("CruiseControl.NET", ReactiveUIdf, "topright")
dev.off()


#SignalR
png(filename="./SignalRc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("SignalR", SignalRdf, "topleft")
dev.off()

png(filename="./SignalRcl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("SignalR", SignalRdf, "topleft")
dev.off()

png(filename="./SignalRcf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("SignalR", SignalRdf, "topleft")
dev.off()

png(filename="./SignalRsc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("SignalR", SignalRdf, "topleft")
dev.off()

#python
#flask
png(filename="./flaskc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("flask", flaskdf, "topleft")
dev.off()

png(filename="./flaskcl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("flask", flaskdf, "topleft")
dev.off()

png(filename="./flaskcf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("flask", flaskdf, "topleft")
dev.off()

png(filename="./flasksc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("flask", flaskdf, "topleft")
dev.off()

#requests
png(filename="./requestsc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("requests", requestsdf, "topleft")
dev.off()

png(filename="./requestscl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("requests", requestsdf, "topleft")
dev.off()

png(filename="./requestscf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("requests", requestsdf, "topright")
dev.off()

png(filename="./requestssc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("requests", requestsdf, "topright")
dev.off()

#scrapy
png(filename="./scrapyc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("scrapy", scrapydf, "topright")
dev.off()

png(filename="./scrapycl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("scrapy", scrapydf, "topright")
dev.off()

png(filename="./scrapycf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("scrapy", scrapydf, "topright")
dev.off()

png(filename="./scrapysc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("scrapy", scrapydf, "topright")
dev.off()

#java
#mockito
png(filename="./mockitoc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("mockito", mockitodf, "topright")
dev.off()

png(filename="./mockitocl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictLinesBarPlot("mockito", mockitodf, "topright")
dev.off()

png(filename="./mockitocf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("mockito", mockitodf, "topright")
dev.off()

png(filename="./mockitosc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictSemanticBarPlot("mockito", mockitodf, "topright")
dev.off()

#dropwizard
png(filename="./dropwizardc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictsBarPlot("dropwizard", dropwizarddf, "topleft")
dev.off()

png(filename="./dropwizardcl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictLinesBarPlot("dropwizard", dropwizarddf, "topleft")
dev.off()

png(filename="./dropwizardcf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2)) 
drawConflictFilesBarPlot("dropwizard", dropwizarddf, "topleft")
dev.off()

png(filename="./dropwizardsc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictSemanticBarPlot("dropwizard", dropwizarddf, "topleft")
dev.off()

#netty
png(filename="./nettyc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictsBarPlot("GenealogyJ", nettydf, "topright")
dev.off()

png(filename="./nettycl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictLinesBarPlot("GenealogyJ", nettydf, "topright")
dev.off()

png(filename="./nettycf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictFilesBarPlot("GenealogyJ", nettydf, "topright")
dev.off()

png(filename="./nettysc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictSemanticBarPlot("GenealogyJ", nettydf, "topright")
dev.off()

#RxJava
png(filename="./RxJavac-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictsBarPlot("RxJava", RxJavadf, "topleft")
dev.off()

png(filename="./RxJavacl-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictLinesBarPlot("RxJava", RxJavadf, "topleft")
dev.off()

png(filename="./RxJavacf-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictFilesBarPlot("RxJava", RxJavadf, "topleft")
dev.off()

png(filename="./RxJavasc-Barplot.png",width=WIDTH,height=HEIGHT)
par(mar=c(6,5,1,2))
drawConflictSemanticBarPlot("RxJava", RxJavadf, "topleft")
dev.off()
}

###################################
# Operations
###################################

### Build DataFrames Start

#build dataframes from input tables
#single tables
mockitodf <- buildDataFrame("mockito", mockitoSM, mockitoUM)
dropwizarddf <- buildDataFrame("dropwizard", dropwizardSM, dropwizardUM)
nettydf <- buildDataFrame("netty", nettySM, nettyUM)
RxJavadf <- buildDataFrame("RxJava", RxJavaSM, RxJavaUM)

#csharp
Nancydf <- buildDataFrame("Nancy", NancySM, NancyUM)
ReactiveUIdf <- buildDataFrame("ReactiveUI", ReactiveUISM, ReactiveUIUM)
SignalRdf <- buildDataFrame("SignalR", SignalRSM, SignalRUM)

#python
flaskdf <- buildDataFrame("flask", flaskSM, flaskUM)
requestsdf <- buildDataFrame("requests", requestsSM, requestsUM)
scrapydf <- buildDataFrame("scrapy", scrapySM, scrapyUM)

### Build DataFrame End
drawBarplotPNGs()

