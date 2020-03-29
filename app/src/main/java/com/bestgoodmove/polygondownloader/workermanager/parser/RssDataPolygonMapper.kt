package com.bestgoodmove.polygondownloader.workermanager.parser


import android.content.ContentValues
import android.util.Log
import com.bestgoodmove.polygondownloader.workermanager.provider.Constants
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.BufferedInputStream
import java.lang.NumberFormatException
import javax.xml.parsers.DocumentBuilderFactory

object RssDataPolygonMapper {

    private val ENTRY = "entry"

    private val TITLE = "title"
    private val LINK = "link"
    private val DESCRIPTION = "description"
    private val PUB_DATE = "published"
    private val UPDATED_DATE = "published"
    private val CONTENT = "content"


    private val TTL = "ttl"

    private val ENCLOSURE = "enclosure"
    private val URL = "url"
    private val LENGTH = "length"
    private val TYPE = "type"

    private var replacer: Replacer? = null
    fun map(inputStream: BufferedInputStream) : ArrayList<ContentValues>?? {
        return map(inputStream, null, {})
    }

    fun map(inputStream: BufferedInputStream, replacer: Replacer) : ArrayList<ContentValues>?? {
        return map(inputStream, replacer, {})
    }

    fun map(inputStream: BufferedInputStream, onFinish: () -> Unit) : ArrayList<ContentValues>?? {
        return map(inputStream, null, onFinish)
    }

    fun map(inputStream: BufferedInputStream, replacer: Replacer?, onFinish: () -> Unit) : ArrayList<ContentValues>? {
        this.replacer = replacer
        var arrContent = ArrayList<ContentValues>();

        try {
            var builderFactory = DocumentBuilderFactory.newInstance()
            var documentBuilder = builderFactory.newDocumentBuilder()
            var document = documentBuilder.parse(inputStream)
            var element = document.documentElement              // getting "rss" tag
            element.normalize()

            var chNodeList = element.getElementsByTagName(ENTRY)  // getting nodes of "channel" tag
            if (chNodeList.length == 0) return null



            var itemsCount = chNodeList.length
            for (i in 0 until itemsCount) {
                var item: Element = chNodeList.item(i) as Element
                var itemTitle = getTitle(item)
                var itemPubDate = getPubDate(item)
                var entryUpdate = getUpdatePubDate(item)
                var content = getContent(item)


                val tuple = ContentValues()
                tuple.put(Constants.TITLE, itemTitle)
                tuple.put(Constants.PUB_DATE, itemPubDate)
                tuple.put(Constants.UPDATED_DATE, entryUpdate)
                tuple.put(Constants.CONTENT,content)

                Log.d("tag", content)
                arrContent.add(tuple);

            }
            return arrContent;

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            try { inputStream.close() } catch (e: Exception) { e.printStackTrace() }
            this.replacer = null
            onFinish.invoke()
        }
    }

    class Replacer(_regexp: String, _replacement: String) {
        val regexp: String = _regexp
        val replacement: String = _replacement
    }

    private fun getTitle(channel: Element) : String? {
        return getNodeValue(getElement(channel, TITLE))
    }

    private fun getLink(channel: Element) : String? {
        return getNodeValue(getElement(channel, LINK))
    }


    private fun getPubDate(channel: Element) : String? {
        return getNodeValue(getElement(channel, PUB_DATE))
    }

    private fun getUpdatePubDate(channel: Element) : String? {
        return getNodeValue(getElement(channel, UPDATED_DATE))
    }


    private  fun getContent(entry: Element): String? {
       return getNodeValue(getElement(entry, CONTENT))
    }



    private fun getNodeValue(element: Element?) : String? {
        if (element == null) return null
        var node: Node? = element.firstChild ?: return null
        var nodeValue = node!!.nodeValue
        if (nodeValue != null && nodeValue.trim().isEmpty()) nodeValue = null
        if (replacer != null && nodeValue != null && nodeValue.trim().isNotEmpty()) {
            nodeValue = nodeValue.trim().replace(replacer!!.regexp, replacer!!.replacement)
        }
        return nodeValue
    }

    private fun getElement(element: Element, name: String) : Element? {
        var nodeList = element.getElementsByTagName(name)
        if (nodeList.length == 0) return null
        return nodeList.item(0) as Element?
    }
}