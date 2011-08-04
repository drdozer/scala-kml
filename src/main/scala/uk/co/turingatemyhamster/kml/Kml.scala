package uk.co.turingatemyhamster
package kml

import javax.swing.text.html.Option
import java.net.URI
import javax.xml.crypto.Data
import java.awt.Color
import math.Fractional
import sun.management.counter.Units
;

/**
 * The Google KML document model.
 *
 * Adapted from http://code.google.com/apis/kml/documentation/kmlreference.html
 *
 * @author Matthew Pocock
 */
package object kml {
type angle180 = Double
}

/**
 * The KML base type.
 *
 * This is an abstract base class and cannot be used directly in a KML file. It provides the id attribute, which allows
 * unique identification of a KML element, and the targetId attribute, which is used to reference objects that have
 * already been loaded into Google Earth. The id attribute must be assigned if the &lt;Update&gt; mechanism is to be
 * used.
 */
sealed trait KmlObject {
  def id: Option[String]
  def targetId: Option[String]
}


/**
 * Entities that are rendered to the user.
 *
 * This is an abstract element and cannot be used directly in a KML file.
 */
sealed trait Feature extends KmlObject {
  /**
   * User-defined text displayed in the 3D viewer as the label for the object (for example, for a Placemark, Folder,
   * or NetworkLink).
   */
  def name: Option[String]

  /**
   * Specifies whether the feature is drawn in the 3D viewer when it is initially loaded. In order for a feature to be
   * visible, the visibility of all its ancestors must also be set to true. Defaults to true.
   */
  def visibility: Option[Boolean]

  /**
   * Specifies whether a Document or Folder appears closed or open when first loaded into the Places panel.
   * False=collapsed (the default), true=expanded. This element applies only to Document, Folder, and NetworkLink.
   *
   * @seeAlso ListStyle
   */
  def open: Option[Boolean]

  /**
   * KML 2.2 supports new elements for including data about the author and related website in your KML file. This
   * information is displayed in geo search results, both in Earth browsers such as Google Earth, and in other
   * applications such as Google Maps. The ascription elements used in KML are as follows:
   *  * atom:author element - parent element for atom:name
   *  * atom:name element - the name of the author
   *  * atom:link element - contains the href attribute
   *  * href attribute - URL of the web page containing the KML/KMZ file
   *
   * These elements are defined in the Atom Syndication Format. The complete specification is found at
   * http://atompub.org.
   *
   * The &lt;atom:author&gt; element is the parent element for &lt;atom:name&gt, which specifies the author of the KML
   * feature.
   */
  def atom_author: Option[atom.Author]

  /**
   * Specifies the URL of the website containing this KML or KMZ file.
   *
   * Implementation note: Be sure to include the namespace for this element in any KML file that uses it:
   * xmlns:atom="http://www.w3.org/2005/Atom" .
   */
  def atom_link: Option[atom.Link]

  /**
   * A string value representing an unstructured address written as a standard street, city, state address, and/or as a
   * postal code. You can use the <address> tag to specify the location of a point instead of using latitude and
   * longitude coordinates. (However, if a <Point> is provided, it takes precedence over the <address>.) To find out
   * which locales are supported for this tag in Google Earth, go to the Google Maps Help.
   */
  def address: Option[String]

  /**
   * A structured address, formatted as xAL, or eXtensible Address Language, an international standard for address
   * formatting. xal.AddressDetails is used by KML for geocoding in Google Maps only. For details, see the Google Maps
   * API documentation. Currently, Google Earth does not use this element; use &lt;address&gt; instead.
   *
   * Implementation note: Be sure to include the namespace for this element in any KML file that uses it:
   * xmlns:xal="urn:oasis:names:tc:ciq:xsdschema:xAL:2.0"
   */
  def addressDetails: Option[xal.AddressDetails]

  /**
   * A string value representing a telephone number. This element is used by Google Maps Mobile only. The industry
   * standard for Java-enabled cellular phones is RFC2806. For more information, see http://www.ietf.org/rfc /rfc2806.txt.
   */
  def phoneNumber: Option[String]

  /**
   * A short description of the feature. In Google Earth, this description is displayed in the Places panel under the
   * name of the feature. If a Snippet is not supplied, the first two lines of the <description> are used. In Google
   * Earth, if a Placemark contains both a description and a Snippet, the <Snippet> appears beneath the Placemark in
   * the Places panel, and the <description> appears in the Placemark's description balloon. This tag does not support
   * HTML markup. <Snippet> has a maxLines attribute, an integer that specifies the maximum number of lines to display.
   */
  def snippet: Option[String]

  /**
   * User-supplied content that appears in the description balloon.
   *
   * The supported content for the <description> element changed from Google Earth 4.3 to 5.0. See the on-line
   * documentation for details.
   */
  def description: Option[String]

  /**
   * Defines a viewpoint associated with any element derived from Feature.
   *
   * @see Camera and LookAt
   */
  def abstractView: Option[AbstractView]

  /**
   * Associates this Feature with a period of time (<TimeSpan>) or a point in time (<TimeStamp>).
   */
  def timePrimitive: Option[TimePrimitive]

  /**
   * URL of a <Style> or <StyleMap> defined in a Document. If the style is in the same file, use a # reference. If the
   * style is defined in an external file, use a full URL along with # referencing.
   */
  def styleUrl: Option[URI]

  /**
   * One or more Styles and StyleMaps can be defined to customize the appearance of any element derived from Feature or
   * of the Geometry in a Placemark.
   *
   * A style defined within a Feature is called an "inline style" and applies only to the Feature that contains it. A
   * style defined as the child of a &lt;Document&gt; is called a "shared style." A shared style must have an id
   * defined for it. This id is referenced by one or more Features within the &lt;Document&gt;. In cases where a style
   * element is defined both in a shared style and in an inline style for a Feature—that is, a Folder, GroundOverlay,
   * NetworkLink, Placemark, or ScreenOverlay—the value for the Feature's inline style takes precedence over the value
   * for the shared style.
   *
   * @see BalloonStyle, ListStyle, StyleSelector, ColorStyle
   */
  def styleSelector: Seq[StyleSelector]

  /**
   * Features and geometry associated with a Region are drawn only when the Region is active.
   */
  def region: Option[Region]

//  /**
//   * Use ExtendedMetadata instead.
//   */
//  @scala.deprecated(message = "No longer in use. use extendedData instead", since = "KML 2.2")
//  def metadata: Option[Metadata]

  /**
   * @since KML 2.2
   */
  def extendedData: Option[ExtendedData]
}

case class ExtendedData(data: Data, schemaData: SchemaData, other: Any*) // other must be serializable to XML
case class Data(name: String, displayName: String, value: String)
case class SchemaData(schemaUrl: URI, simpleData: SimpleData*)
case class SimpleData(name: String, value: String)


case class NetworkLink(id: Option[String] = None, targetId: Option[String] = None,
                       visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                       atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                       address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                       phoneNumber: Option[String] = None, snippet: Option[String] = None,
                       description: Option[String] = None, abstractView: Option[AbstractView] = None,
                       timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                       styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                       extendedData: ExtendedData = None,
                       /**
                        *  A value of false leaves the visibility of features within the control of the Google Earth
                        *  user. Set the value to true to reset the visibility of features each time the NetworkLink is
                        *  refreshed. For example, suppose a Placemark within the linked KML file has &lt;visibility&gt;
                        *  set to true and the NetworkLink has &lt;refreshVisibility&gt; set to true. When the file is
                        *  first loaded into Google Earth, the user can clear the check box next to the item to turn off
                        *  display in the 3D viewer. However, when the NetworkLink is refreshed, the Placemark will be
                        *  made visible again, since its original visibility state was TRUE.
                        *
                        *  Default: false
                        */
                       refreshVisibility: Option[Boolean],
                       /**
                        * A value of true causes Google Earth to fly to the view of the LookAt or Camera in the
                        * NetworkLinkControl (if it exists). If the NetworkLinkControl does not contain an AbstractView
                        * element, Google Earth flies to the LookAt or Camera element in the Feature child within the
                        * &lt;kml&gt; element in the refreshed file. If the &lt;kml&gt; element does not have a LookAt
                        * or Camera specified, the view is unchanged. For example, Google Earth would fly to the
                        * &lt;LookAt&gt; view of the parent Document, not the &lt;LookAt&gt; of the Placemarks contained
                        * within the Document.
                        *
                        * Default: false
                        */
                       flyToView: Option[Boolean],
                       /** The Link to act on */
                       link: Link ) extends Feature

/**
 * Network location with added annotation.
 *
 * &lt;Link&gt; specifies the location of any of the following:
 *  * KML files fetched by network links
 *  * Image files used in any Overlay (the <Icon> element specifies the image in an Overlay; <Icon> has the same fields
 *  as <Link>)
 *  * Model files used in the <Model> element
 *
 * The file is conditionally loaded and refreshed, depending on the refresh parameters supplied here. Two different sets
 * of refresh parameters can be specified: one set is based on time (<refreshMode> and <refreshInterval>) and one is
 * based on the current "camera" view (<viewRefreshMode> and <viewRefreshTime>). In addition, Link specifies whether to
 * scale the bounding box parameters that are sent to the server (<viewBoundScale> and provides a set of optional
 * viewing parameters that can be sent to the server (<viewFormat>) as well as a set of optional parameters containing
 * version and language information.
 *
 * When a file is fetched, the URL that is sent to the server is composed of three pieces of information:
 *  * the href (Hypertext Reference) that specifies the file to load.
 *  * an arbitrary format string that is created from (a) parameters that you specify in the <viewFormat> element or
 *  (b) bounding box parameters (this is the default and is used if no <viewFormat> element is included in the file).
 *  * a second format string that is specified in the <httpQuery> element.
 *
 * If the file specified in <href> is a local file, the <viewFormat> and <httpQuery> elements are not used.
 *
 * The <Link> element replaces the <Url> element of <NetworkLink> contained in earlier KML releases and adds
 * functionality for the <Region> element (introduced in KML 2.1). In Google Earth releases 3.0 and earlier, the Link
 * element is ignored.
 */
case class Link(id: Option[String], targetId: Option[String],
                /**
                 * A URL (either an HTTP address or a local file specification). When the parent of <Link> is a
                 * NetworkLink, <href> is a KML file. When the parent of <Link> is a Model, <href> is a COLLADA file.
                 * When the parent of <Icon> (same fields as <Link>) is an Overlay, <href> is an image. Relative URLs
                 * can be used in this tag and are evaluated relative to the enclosing KML file. See KMZ Files for
                 * details on constructing relative references in KML and KMZ files.
                 */
                href: URI,
                /** The refresh mode.
                 * Defaults to: OnChange.
                 */
                refreshMode: RefreshMode,
                /** Refresh the file every 'refreshInterval' seconds. */
                refreshInterval: Option[Double],
                /**
                 * Specifies how the link is refreshed when the "camera" changes.
                 * Defaults to: Never
                 */
                viewRefreshMode: ViewRefreshMode,
                /** After camera movement stops, specifies the number of seconds to wait before refreshing the view. */
                viewRefreshTime: Double,

                /**
                 * Scales the BBOX parameters before sending them to the server. A value less than 1 specifies to use
                 * less than the full view (screen). A value greater than 1 specifies to fetch an area that extends
                 * beyond the edges of the current view.
                 */
                viewBoundScale: Double,

                /**
                 * Specifies the format of the query string that is appended to the Link's <href> before the file is
                 * fetched.
                 *
                 * Defaults to: [bboxWest],[bboxSouth],[bboxEast],[bboxNorth]
                 */
                viewFormat: Option[String],

                /**Appends information to the query string, based on the parameters specified. */
                httpQuery: Option[String]
                 ) extends KmlObject

/**
 * A time-based refresh mode.
 */
sealed trait RefreshMode
/** Refresh when the file is loaded and whenever the Link parameters change. */
case object OnChange extends RefreshMode
/** Refresh every n seconds (given in refreshInterval of the containing Link). */
case object OnInterval extends RefreshMode
/** Refresh when the expiration time is reached.  */
case object OnExpire extends RefreshMode

/** Specifies how the link is refreshed when the "camera" changes. */
sealed trait ViewRefreshMode
/** Ignore changes in the view. Also ignore <viewFormat> parameters, if any. */
case object Never extends ViewRefreshMode
/** Refresh the file n seconds after movement stops, where n is specified in <viewRefreshTime>. */
case object OnStop extends ViewRefreshMode
/** Refresh the file only when the user explicitly requests it. (For example, in Google Earth, the user right-clicks and
 *  selects Refresh in the Context menu.) */
case object OnRequest extends ViewRefreshMode
/** Refresh the file when the Region becomes active. */
case object OnRegsion extends ViewRefreshMode


/** The base type for image overlays drawn on the planet surface or on the screen. */
sealed trait Overlay extends Feature {
  /** ABGR (aabbggrr) Color.
   *
   * Defaults to: ffffffff
   */
  def color: Option[Color]

  /**
   * the stacking order for the images in overlapping overlays. Overlays with higher &lt;drawOrder&gt; values are drawn
   * on top of overlays with lower &lt;drawOrder&gt; values.
   *
   * Defaults to: 0
   */
  def drawOrder: Option[Int]

  def icon: Option[Icon]
}

case class Icon(id: Option[String], targetId: Option[String],
                /** Location of the Icon image */
                href: URI,
                /** If the href specifies an icon pallet, this element gives the x-offset in pixels. */
                gx_x: Int,
                /** If the href specifies an icon pallet, this element gives the x-offset in pixels. */
                gx_y: Int,
                /** If the href specifies an icon pallet, this element gives the width in pixels. */
                gx_w: Int,
                /** If the href specifies an icon pallet, this element gives the height in pixels. */
                gx_h: Int,
                /** The refresh mode.
                 * Defaults to: OnChange.
                 */
                refreshMode: RefreshMode,
                /** Refresh the file every 'refreshInterval' seconds. */
                refreshInterval: Option[Double],
                /**
                 * Specifies how the link is refreshed when the "camera" changes.
                 * Defaults to: Never
                 */
                viewRefreshMode: ViewRefreshMode,
                /** After camera movement stops, specifies the number of seconds to wait before refreshing the view. */
                viewRefreshTime: Double,

                /**
                 * Scales the BBOX parameters before sending them to the server. A value less than 1 specifies to use
                 * less than the full view (screen). A value greater than 1 specifies to fetch an area that extends
                 * beyond the edges of the current view.
                 */
                viewBoundScale: Double,

                /**
                 * Specifies the format of the query string that is appended to the Link's <href> before the file is
                 * fetched.
                 *
                 * Defaults to: [bboxWest],[bboxSouth],[bboxEast],[bboxNorth]
                 */
                viewFormat: Option[String],

                /**Appends information to the query string, based on the parameters specified. */
                httpQuery: Option[String]
                 ) extends KmlObject

/**
 * Geographically locate a photograph on the Earth and to specify viewing parameters for this PhotoOverlay.
 *
 * The PhotoOverlay can be a simple 2D rectangle, a partial or full cylinder, or a sphere (for spherical panoramas).
 * The overlay is placed at the specified location and oriented toward the viewpoint.
 *
 * Because PhotoOverlay is derived from Feature, it can contain one of the two elements derived from
 * AbstractView—either Camera or LookAt. The Camera (or LookAt) specifies a viewpoint and a viewing direction (also
 * referred to as a view vector). The PhotoOverlay is positioned in relation to the viewpoint. Specifically, the plane
 * of a 2D rectangular image is orthogonal (at right angles to) the view vector. The normal of this plane—that is, its
 * front, which is the part with the photo—is oriented toward the viewpoint.
 *
 * The URL for the PhotoOverlay image is specified in the Icon tag, which is inherited from Overlay. The Icon tag must
 * contain an href element that specifies the image file to use for the PhotoOverlay. In the case of a very large image,
 * the href is a special URL that indexes into a pyramid of images of varying resolutions (see ImagePyramid).
 *
 * For more information, see the "Topics in KML" page on PhotoOverlay.
 */
case class PhotoOverlay(id: Option[String] = None, targetId: Option[String] = None,
                       visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                       atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                       address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                       phoneNumber: Option[String] = None, snippet: Option[String] = None,
                       description: Option[String] = None, abstractView: Option[AbstractView] = None,
                       timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                       styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                       extendedData: ExtendedData = None,
                       /** Image rotation. Defaults to 0. */
                       rotation: angle180,
                       /** Defines how much of the current scene is visible. */
                       viewVolume: ViewVolume,
                       /** A hierarchical set of images, each of which is an increasingly lower resolution version of
                        *  the original image. */
                       imagePyramid: ImagePyramid,

                       /**The point acts as a point inside a placemark element. It draws an icon to mark the position
                        * of the PhotoOverlay. The icon drawn is pecified by the styleurl and StyleSelector fields, just
                        * as it is for Placemark.
                        */
                       point: Point,

                       /**The PhotoOverlay is projected onto the shape. Defaults to: rectangle */
                       shape: Shape
                       ) extends Overlay


/**
 * Defines how much of the current scene is visible. Specifying the field of view is analogous to specifying the lens
 * opening in a physical camera. A small field of view, like a telephoto lens, focuses on a small part of the scene. A
 * large field of view, like a wide-angle lens, focuses on a large part of the scene.
 */
case class ViewVolume(/** Angle, in degrees, between the camera's viewing direction and the left side of the view
                      volume. */
                      leftFov: angle180,
                      /** Angle, in degrees, between the camera's viewing direction and the right side of the view
                       * volume. */
                      rightFov: angle180,

                      /**Angle, in degrees, between the camera's viewing direction and the bottom side of the view
                       * volume. */
                      bottomFov: angle180,
                      /** Angle, in degrees, between the camera's viewing direction and the top side of the view
                       *  volume. */
                      topFov: angle180,

                      /**Measurement in meters along the viewing direction from the camera viewpoint to the
                       * PhotoOverlay shape. */
                      near: Double)

/**
 * For very large images, you'll need to construct an image pyramid, which is a hierarchical set of images, each of
 * which is an increasingly lower resolution version of the original image. Each image in the pyramid is subdivided into
 * tiles, so that only the portions in view need to be loaded. Google Earth calculates the current viewpoint and loads
 * the tiles that are appropriate to the user's distance from the image. As the viewpoint moves closer to the
 * PhotoOverlay, Google Earth loads higher resolution tiles. Since all the pixels in the original image can't be viewed
 * on the screen at once, this preprocessing allows Google Earth to achieve maximum performance because it loads only
 * the portions of the image that are in view, and only the pixel details that can be discerned by the user at the
 * current viewpoint.
 *
 * When you specify an image pyramid, you also modify the <href> in the <Icon> element to include specifications for
 * which tiles to load.
 */
case class ImagePyramid(/** Size of the tiles, in pixels. Tiles must be square, and <tileSize> must be a power of 2. A
                        tile size of 256 (the default) or 512 is recommended. The original image is divided into tiles
                        of this size, at varying resolutions. */
                        tileSize: Int,
                        /** Width in pixels of the original image. */
                        maxWidth: Int,
                        /** Height in pixels of the original image. */
                        maxHeight: Int,
                        /** Specifies where to begin numbering the tiles in each layer of the pyramid. A value of
                         *  lowerLeft specifies that row 1, column 1 of each layer is in the bottom left corner of the
                         *  grid. */
                        gridOrigin: GridOrigin
                         )

/**
 * The PhotoOverlay is projected onto the shape.
 */
sealed trait Shape
/** For an ordinary photo. */
case object Rectangle extends Shape
/** For panoramas, which can be either partial or full cylinders */
case object Cylinder extends Shape
/** For spherical panoramas. */
case object Sphere

sealed trait GridOrigin
case object LowerLeft extends GridOrigin
case object UpperLeft extends GridOrigin

/**
 * An image overlay fixed to the screen.
 *
 * Sample uses for ScreenOverlays are compasses, logos, and heads-up displays. ScreenOverlay sizing is determined by the
 * size element. Positioning of the overlay is handled by mapping a point in the image specified by overlayXY to a point
 * on the screen specified by screenXY. Then the image is rotated by rotation degrees about a point relative to the
 * screen specified by rotationXY.
 *
 * The href child of Icon specifies the image to be used as the overlay. This file can be either on a local file system
 * or on a web server. If this element is omitted or contains no href, a rectangle is drawn using the color and size
 * defined by the screen overlay.
 */
case class ScreenOverlay(id: Option[String] = None, targetId: Option[String] = None,
                       visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                       atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                       address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                       phoneNumber: Option[String] = None, snippet: Option[String] = None,
                       description: Option[String] = None, abstractView: Option[AbstractView] = None,
                       timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                       styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                       extendedData: ExtendedData = None,
                       /**
                        *  Specifies a point on (or outside of) the overlay image that is mapped to the screen
                        *  coordinate (screenXY). It requires x and y values, and the units for those values.
                        *
                        *  The x and y values can be specified in three different ways: as pixels ("pixels"), as
                        *  fractions of the image ("fraction"), or as inset pixels ("insetPixels"), which is an offset
                        *  in pixels from the upper right corner of the image. The x and y positions can be specified in
                        *  different ways—for example, x can be in pixels and y can be a fraction. The origin of the
                        *  coordinate system is in the lower left corner of the image.
                        *   * x - Either the number of pixels, a fractional component of the image, or a pixel inset
                        *   indicating the x component of a point on the overlay image.
                        *   * y - Either the number of pixels, a fractional component of the image, or a pixel inset
                        *   indicating the y component of a point on the overlay image.
                        *   * xunits - Units in which the x value is specified. A value of "fraction" indicates the x
                        *   value is a fraction of the image. A value of "pixels" indicates the x value in pixels. A
                        *   value of "insetPixels" indicates the indent from the right edge of the image.
                        *   * yunits - Units in which the y value is specified. A value of "fraction" indicates the y
                        *   value is a fraction of the image. A value of "pixels" indicates the y value in pixels. A
                        *   value of "insetPixels" indicates the indent from the top edge of the image.
                        */
                       overlayXY: XY,

                       /**
                        * Specifies a point relative to the screen origin that the overlay image is mapped to. The x and
                        * y values can be specified in three different ways: as pixels ("pixels"), as fractions of the
                        * screen ("fraction"), or as inset pixels ("insetPixels"), which is an offset in pixels from the
                        * upper right corner of the screen. The x and y positions can be specified in different
                        * ways—for example, x can be in pixels and y can be a fraction. The	origin of the coordinate
                        * system is in the lower left corner of the screen.
                        *   * x - Either the number of pixels, a fractional component of the screen, or a pixel inset
                        *   indicating the x component of a point on the screen.
                        *   * y - Either the number of pixels, a fractional component of the screen, or a pixel inset
                        *   indicating the y component of a point on the screen.
                        *   * xunits - Units in which the x value is specified. A value of "fraction" indicates the x
                        *   value is a fraction of the screen. A value of "pixels" indicates the x value in pixels. A
                        *   value of "insetPixels" indicates the indent from the right edge of the screen.
                        *   * yunits - Units in which the y value is specified. A value of fraction indicates the y
                        *   value is a fraction of the screen. A value of "pixels" indicates the y value in pixels. A
                        *   value of "insetPixels" indicates the indent from the top edge of the screen.
                        */
                       screenXY: XY,

                       /**
                        * Point relative to the screen about which the screen overlay is rotated.
                        */
                       rotationXY: XY,

                       /**
                        * Specifies the size of the image for the screen overlay, as follows:
                        *   * A value of −1 indicates to use the native dimension
                        *   * A value of 0 indicates to maintain the aspect ratio
                        *   * A value of n sets the value of the dimension
                        */
                       sizeXY: XY,

                       /**
                        * Indicates the angle of rotation of the parent object. A value of 0 means no rotation. The
                        * value is an angle in degrees counterclockwise starting from north. Use ±180 to indicate the
                        * rotation of the parent object from 0. The center of the <rotation>, if not (.5,.5), is
                        * specified in rotationXY.
                        */
                       rotation: Option[Double]
                          ) extends Overlay

case class XY(x: Double, y: Double, xunits: Units = Fractional, yunits: Units = Fractional)

sealed trait Units
case object Fractional extends Units
case object Pixels extends Units
case object InsetPixels extends Units


/**
 * This element draws an image overlay draped onto the terrain. The <href> child of <Icon> specifies the image to be
 * used as the overlay. This file can be either on a local file system or on a web server. If this element is omitted or
 * contains no <href>, a rectangle is drawn using the color and LatLonBox bounds defined by the ground overlay.
 */
case class GroundOverlay(id: Option[String] = None, targetId: Option[String] = None,
                       visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                       atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                       address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                       phoneNumber: Option[String] = None, snippet: Option[String] = None,
                       description: Option[String] = None, abstractView: Option[AbstractView] = None,
                       timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                       styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                       extendedData: ExtendedData = None,
                       /**
                        * Specifies the distance above the earth's surface, in meters, and is interpreted according to
                        * the altitude mode.
                        */
                       altitude: Double,

                       /**
                        * Specifies how the altitude is interpreted.
                        * Defaults to: ClampToGround
                        */
                       altitudeMode: Option[AltitudeMode],
                       latLonBox: Option[LatLonBox],
                       latLonQuad: Option[gx.LatLonQuad]
                          ) extends Overlay

/**
 * Specifies the distance above the earth's surface, in meters, and is interpreted according to the altitude mode.
 */
sealed trait AltitudeMode

/**
 * Indicates to ignore the altitude specification and drape the overlay over the terrain.
 */
case object ClampToGround extends AltitudeMode

/**
 * Specifies where the top, bottom, right, and left sides of a bounding box for the ground overlay are aligned.
 */
case class LatLonBox(/** Specifies the latitude of the north edge of the bounding box, in decimal degrees from 0 to
                     *   ±90. */
                     north: Double,
                     /** Specifies the latitude of the south edge of the bounding box, in decimal degrees from 0 to
                      *  ±90. */
                     south: Double,
                     /** Specifies the longitude of the east edge of the bounding box, in decimal degrees from 0 to
                      *  ±180. (For overlays that overlap the meridian of 180° longitude, values can extend beyond that
                      *  range.) */
                     east: Double,
                     /** Specifies the longitude of the west edge of the bounding box, in decimal degrees from 0 to
                      *  ±180. (For overlays that overlap the meridian of 180° longitude, values can extend beyond that
                      *  range.) */
                     west: Double,
                     /** Specifies a rotation of the overlay about its center, in degrees. Values can be ±180. The
                      *  default is 0 (north). Rotations are specified in a counterclockwise direction. */
                      rotation: angle180)

/**
 * Sets the altitude of the overlay relative to sea level, regardless of the actual elevation of the terrain beneath the
 * element. For example, if you set the altitude of an overlay to 10 meters with an absolute altitude mode, the overlay
 * will appear to be at ground level if the terrain beneath is also 10 meters above sea level. If the terrain is 3
 * meters above sea level, the overlay will appear elevated above the terrain by 7 meters.
 */
case object Absolute extends AltitudeMode


/**
 * A Container element holds one or more Features and allows the creation of nested hierarchies.
 */
sealed trait Container extends Feature {
  /** Zero or more contained features */
  def features: Seq[Feature]
}


/**
 * A Folder is used to arrange other Features hierarchically (Folders, Placemarks, NetworkLinks, or Overlays). A Feature
 * is visible only if it and all its ancestors are visible.
 */
case class Folder(id: Option[String] = None, targetId: Option[String] = None,
                  visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                  atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                  address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                  phoneNumber: Option[String] = None, snippet: Option[String] = None,
                  description: Option[String] = None, abstractView: Option[AbstractView] = None,
                  timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                  styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                  extendedData: ExtendedData = None,
                  features: Seq[Feature]
                   ) extends Container

/**
 * A Document is a container for features and styles. This element is required if your KML file uses shared styles. It
 * is recommended that you use shared styles, which require the following steps:
 *  # Define all Styles in a Document. Assign a unique ID to each Style.
 *  # Within a given Feature or StyleMap, reference the Style's ID using a styleUrl element.
 * Note that shared styles are not inherited by the Features in the Document.
 *
 * Each Feature must explicitly reference the styles it uses in a styleUrl element. For a Style that applies to a
 * Document (such as ListStyle), the Document itself must explicitly reference the styleUrl.
 */
case class Document(id: Option[String] = None, targetId: Option[String] = None,
                  visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                  atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                  address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                  phoneNumber: Option[String] = None, snippet: Option[String] = None,
                  description: Option[String] = None, abstractView: Option[AbstractView] = None,
                  timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                  styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                  extendedData: ExtendedData = None,
                  schemas: seq[Schema],
                  features: Seq[Feature]) extends Container

case class Schema(id: Option[String] = None, targetId: Option[String] = None,
                  name: String,
                  simpleField: SimpleField
                   ) extends KmlObject

case class SimpleField(type: String, name: String, displayName: String)

sealed trait Geometry extends KmlObject

case class Point extends Geometry
case class LineString extends Geometry
case class LinearRing extends Geometry
case class Polygon extends Geometry
case class MultiGeometry extends Geometry
case class Model extends Geometry

sealed trait StyleSelector extends KmlObject

case class Style extends StyleSelector
case class StyleMap extends StyleSelector

sealed trait TimePrimitive

case class TimeSpan extends TimePrimitive
case class TimeStamp extends TimePrimitive

sealed trait AbstractView extends KmlObject

case class Camera extends AbstractView
case class LookAt extends AbstractView

case class Region extends KmlObject
case class Lod extends KmlObject
case class LatLongBox extends KmlObject
case class LatLongAltBox extends KmlObject

sealed trait SubStyle

case class BalloonStyle extends SubStyle
case class ListStyle extends SubStyle

sealed trait ColorStyle extends SubStyle

case class LineStyle extends ColorStyle
case class PolyStyle extends ColorStyle
case class IconStyle extends ColorStyle
case class LabelStyle extends ColorStyle

case class LatLon(lat: Double, lon: Double)

/**
 * These entities are an extension of the OGC KML 2.2 standard and are supported in Google Earth 5.0 and later.
 *
 * http://code.google.com/apis/kml/documentation/kmlreference.html#kmlextensions
 */
package gx {

sealed trait TourPrimitive extends KmlObject

/**
 * Contains a single PlayList, which define a tour in any KML browser.
 */
case class Tour(id: Option[String] = None, targetId: Option[String] = None,
                visibility: Option[Boolean] = None, open: Option[Boolean] = None,
                atom_author: Option[atom.Author] = None, atom_link: Option[atom.Link] = None,
                address: Option[String] = None, addressDetails: Option[xal.AddressDetails] = None,
                phoneNumber: Option[String] = None, snippet: Option[String] = None,
                description: Option[String] = None, abstractView: Option[AbstractView] = None,
                timePrimitive: Option[TimePrimitive] = None, styleUrl: Option[URI] = None,
                styleSelector: Option[StyleSelector] = None, region: Option[Region] = None,
                extendedData: ExtendedData = None,
                /** The playlist for this tour. */
                playList: PlayList) extends Feature

/**
 * A KML extension in the Google extension namespace, allowing altitudes relative to the sea floor.
 */
sealed trait AltitudeMode extends kml.AltitudeMode


/**
 * Indicates to ignore the altitude specification and drape the overlay over the terrain.
 */
case object RelativeToSeaFloor

/**
 * Sets the altitude of the overlay relative to sea level, regardless of the actual elevation of the terrain beneath the
 * element. For example, if you set the altitude of an overlay to 10 meters with an absolute altitude mode, the overlay
 * will appear to be at ground level if the terrain beneath is also 10 meters above sea level. If the terrain is 3
 * meters above sea level, the overlay will appear elevated above the terrain by 7 meters.
 */
case object Absolute

case class Track extends Geometry
case class MultiTrack extends Geometry

case class TimeSpan extends TimePrimitive
case class TimeStamp extends TimePrimitive

/**
 * Allows nonrectangular quadrilateral ground overlays.
 *
 * Specifies the coordinates of the four corner points of a quadrilateral defining the overlay area. Exactly four
 * coordinate tuples have to be provided, each consisting of floating point values for longitude and latitude. Insert a
 * space between tuples. Do not include spaces within a tuple. The coordinates must be specified in counter-clockwise
 * order with the first coordinate corresponding to the lower-left corner of the overlayed image. The shape described by
 * these corners must be convex.
 *
 * If a third value is inserted into any tuple (representing altitude) it will be ignored. Altitude is set using
 * altitude and altitudeMode (or gx:altitudeMode) extending GroundOverlay. Allowed altitude modes are absolute,
 * clampToGround, and clampToSeaFloor.
 */
case class LatLongQuad(id: Option[String] = None, targetId: Option[String] = None,
                /** A sequence of 4 lattitude/longitude coordinates. */
                coordinates: Seq[LatLon]) extends KmlObject

case class AnimatedUpdate extends TourPrimitive
case class FlyTo extends TourPrimitive
case class SoundCue extends TourPrimitive
case class Wait extends TourPrimitive

case class PlayList extends KmlObject

}

